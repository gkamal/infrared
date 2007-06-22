package net.sf.infrared.tool.mojo;

import java.io.File;

import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.project.MavenProject;

public  class InfraredMojoTestBase {
	
	void installPlugin() throws Exception{
		MavenEmbedder embedder = new MavenEmbedder();
		embedder.setClassLoader(Thread.currentThread().getContextClassLoader());
		try{
			embedder.start();
			MavenProject project = embedder.readProject(new File(getProjectDir(),"pom.xml"));
		}finally {
			//embedder.stop();
		}
	}

	private File getProjectDir() {
		String base = System.getProperty("user.dir");
		return new File(base);
	}
	
	public static void main(String[] args) throws Exception {
		InfraredMojoTestBase b = new InfraredMojoTestBase();
		b.installPlugin();
	}

}
