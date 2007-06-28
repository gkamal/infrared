/* 
 * Copyright 2005 Tavant Technologies and Contributors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package net.sf.infrared.tool.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

/**
 * Provides utility services for jarring and unjarring files and directories.
 * Note that a given instance of JarHelper is not threadsafe with respect to
 * multiple jar operations.
 * 
 * @author Patrick Calahan <pcal@bea.com>
 */
public class JarHelper {
    // ========================================================================
    // Constants

    private static final int BUFFER_SIZE = 64;

    // ========================================================================
    // Variables

    private byte[] mBuffer = new byte[BUFFER_SIZE];

    private int mByteCount = 0;

    private boolean mVerbose = false;

    // ========================================================================
    // Constructor

    /**
     * Instantiates a new JarHelper.
     */
    public JarHelper() {
    }

    // ========================================================================
    // Public methods

    /**
     * Jars a given directory or single file into a JarOutputStream.
     */
    public void jarDir(File dirOrFile2Jar, File destJar) throws IOException {
        JarOutputStream jout = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(destJar)));
        // jout.setLevel(0);
        try {
            jarDir(dirOrFile2Jar, jout, null);
        } finally {
            jout.close();
        }
    }
    
    /**
     * Jars a given array of directories or files into a JarOutputStream.
     */
    public void jarDirs(File[] dirsOrFiles2Jar, File destJar) throws IOException {
    	JarOutputStream jout = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(destJar)));
        try {
            for (int i = 0; i < dirsOrFiles2Jar.length; i++) {
                jarDir(dirsOrFiles2Jar[i], jout, null);
            }
        } finally {
            jout.close();
        }
    }

    /**
     * Unjars a given jar file into a given directory.
     */
    public void unjarDir(File jarFile, File destDir) throws IOException {
        InputStream fis = new BufferedInputStream(new FileInputStream(jarFile));
        unjar(fis, destDir);
    }

    /**
     * Given an InputStream on a jar file, unjars the contents into the given
     * directory.
     */
    public void unjar(InputStream in, File destDir) throws IOException {
        BufferedOutputStream dest = null;
        JarInputStream jis = new JarInputStream(in);
        JarEntry entry;
        while ((entry = jis.getNextJarEntry()) != null) {
            if (entry.isDirectory()) {
                File dir = new File(destDir, entry.getName());
                dir.mkdir();
                if (entry.getTime() != -1)
                    dir.setLastModified(entry.getTime());
                continue;
            }
            int count;
            byte data[] = new byte[BUFFER_SIZE];
            File destFile = new File(destDir, entry.getName());
            if (!destFile.exists()) {
                String tempStr = destFile.getParent();
                File tempFile = new File(tempStr);
                tempFile.mkdirs();
            }
            if (mVerbose)
                System.out.println("unjarring " + destFile + " from " + entry.getName());
            FileOutputStream fos = new FileOutputStream(destFile);
            dest = new BufferedOutputStream(fos, BUFFER_SIZE);
            while ((count = jis.read(data, 0, BUFFER_SIZE)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
            if (entry.getTime() != -1)
                destFile.setLastModified(entry.getTime());
        }
        jis.close();
    }

    public void setVerbose(boolean b) {
        mVerbose = b;
    }

    // ========================================================================
    // Private methods

    private static final char SEP = '/';

    /**
     * Recursively jars up the given path under the given directory.
     */
    private void jarDir(File dirOrFile2jar, JarOutputStream jos, String path) throws IOException {
        if (mVerbose)
            System.out.println("checking " + dirOrFile2jar);
        if (dirOrFile2jar.isDirectory()) {
            String[] dirList = dirOrFile2jar.list();
            String subPath = (path == null) ? "" : (path + dirOrFile2jar.getName() + SEP);
            if (path != null) {
                JarEntry je = new JarEntry(subPath);
                je.setTime(dirOrFile2jar.lastModified());
                jos.putNextEntry(je);
                jos.flush();
                jos.closeEntry();
            }
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(dirOrFile2jar, dirList[i]);
                jarDir(f, jos, subPath);
            }
        } else {
            if (mVerbose)
                System.out.println("adding " + dirOrFile2jar);
            FileInputStream fis = new FileInputStream(dirOrFile2jar);
            try {
                JarEntry entry = new JarEntry(path + dirOrFile2jar.getName());
                entry.setTime(dirOrFile2jar.lastModified());
                jos.putNextEntry(entry);
                while ((mByteCount = fis.read(mBuffer)) != -1) {
                    jos.write(mBuffer, 0, mByteCount);
                    if (mVerbose)
                        System.out.println("wrote " + mByteCount + " bytes");
                }
                jos.flush();
                jos.closeEntry();
            } finally {
                fis.close();
            }
        }
    }
    
    
    public boolean containsFile(File jarFile, String entryName,boolean absolute) throws IOException {
		JarInputStream jis = new JarInputStream(new BufferedInputStream(new FileInputStream(jarFile)));
		JarEntry entry = null;
		while ((entry = jis.getNextJarEntry()) != null) {
           if (absolute	&& entry.getName().equals(entryName)) {
	        	return true;
	        }
           if (!absolute  && entry.getName().endsWith(entryName)) {
        	   return true;
           }
		}
		return false;
	}
}
