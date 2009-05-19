/*
 *
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
 *
 *
 * Original Author:  G.Schulz (SCC)    Erstversion
 * Contributor(s):   sreejith.unnikrishnan (Tavant Technologies);
 *					
 *
 */

/*
+ ---------------------------------------------------------------------------------+
| Object...: Browser()
| Zweck....: Dient zur Abfrage der Browsereinstellungen
|
| Datum       Author            Bemerkung
| ----------  ----------------  ----------------------------------------------------
| 23.12.2002  G.Schulz (SCC)    First version
| 31.12.2003  G.Schulz (SCC)    test for opera browser added
|
+ ---------------------------------------------------------------------------------+
*/
function Browser() {
}
function Browser_getAppName() {
	return navigator.appName;
}
function Browser_getAppVersion() {
	return navigator.appVersion;
}
function Browser_getUserAgent() {
	return navigator.userAgent;
}
function Browser_isNS() {
	return this.getAppName().indexOf('Netscape') != -1;
}
function Browser_isIE() {
	if (this.isOpera()) {
		return false;
	} else {
		return this.getAppName().indexOf('Microsoft') != -1;
	}
}
function Browser_isOpera() {
	return this.getUserAgent().indexOf('Opera') != -1;
}
function Browser_extractVersion() {
	var out = '';
	var ua = this.getUserAgent();

	if (this.isNS()) {
		var arr = ua.split(' ');
		for (var i=1; i <= arr.length; i++) {
			if ( arr[i].indexOf('Netscape') != -1) {
				return arr[i].split('/')[1];
			}
		}
	}
	
	if (this.isIE()) {
		var arr = ua.split(';');
		for (var i=1; i <= arr.length; i++) {
			if ( arr[i].indexOf('MSIE') != -1) {
				return arr[i].split(' ')[2];
			}
		}
	}
	
	if (this.isOpera()) {
		// Opera.([\d]+\.[\d]+)
		var arr = ua.split('Opera');
		return arr[1].split('[')[0];
	}

	// Default
	return out;
}
function Browser_getPlatform() {
	return navigator.platform;
}
function Browser_isSupported() {
	// Our Application Supports IE >= 4.7 and NS >= 7.0
	var version = this.extractVersion();
	
	if (this.isIE() && parseFloat(version) > 5) {
		return true;
	}
	if (this.isNS() && parseFloat(version) > 7) {
		return true;
	}
	if (this.isOpera() && parseFloat(version) > 7) {
		return true;
	}
	
	return false;
}
function Browser_getPlugInList() {
	var out = '';
	var _plugin;
	
	navigator.plugins.refresh();
	
	if (navigator.plugins.length == 0) {
		if (this.isIE) {
			out = 'Information not available';
		} else {
			out = 'none';
		}
	} else {
		for(var i=0; i < navigator.plugins.length; i++) {
			_plugin = navigator.plugins[i];
			out += _plugin.name + ' (' + _plugin.description + ')' + '<BR>';
		}
	}
	return out;
}
function Browser_getMimeTypeList() {
	var out = '';
	
	if (navigator.mimeTypes.length == 0) {
		out = 'none';
	} else {
		for(var i=0; i < navigator.mimeTypes.length; i++) {
			var _mimeTypes = navigator.mimeTypes[i];
			out += _mimeTypes.name + ' (' + _mimeTypes.description + ')' + '<BR>';
		}
	}
	return out;
}
function Browser_isCookieEnabled() {
	return navigator.cookieEnabled;
}
function Browser_toString() {
	var out = '';
	out += '****** Browser *******' + LF;
	out += 'AppName.......: ' + this.getAppName() + LF;
	out += 'AppVersion....: ' + this.getAppVersion() + LF;
	out += 'Platform......: ' + this.getPlatform() + LF;
	out += 'userAgent.....: ' + this.getUserAgent() + LF;
	out += 'IE............: ' + this.isIE() + LF;
	out += 'NS............: ' + this.isNS() + LF;
	out += 'Opera.........: ' + this.isOpera() + LF;
	out += 'Version.......: ' +	this.extractVersion() + LF;
	out += 'Is Supported..: ' +	this.isSupported();
	return out;
}
function Browser_javaEnabled() {
	return navigator.javaEnabled();
}
function Browser_getJavaEnabledMessage() {
	if (this.javaEnabled()) {
		return 'Yes';
	} else {
		var info = "Warning!<br>";
		var txt = "No JavaScript enabled. JavaScript is required to run the Application.";
		txt += "<br><b>Please turn on JavaScript in your Browser.</b>&nbsp;<a href='help/jscript/ie/enablejscript.html' target='_blank' class='help'>[Help...]</a>";
		return info.fontcolor('red').bold() + txt;
	}
}
new Browser();
Browser.getAppName            = Browser_getAppName;
Browser.getAppVersion         = Browser_getAppVersion;
Browser.getUserAgent          = Browser_getUserAgent;
Browser.extractVersion        = Browser_extractVersion;
Browser.getPlatform           = Browser_getPlatform;
Browser.javaEnabled           = Browser_javaEnabled;
Browser.getJavaEnabledMessage = Browser_getJavaEnabledMessage;
Browser.isNS                  = Browser_isNS;
Browser.isIE                  = Browser_isIE;
Browser.isOpera               = Browser_isOpera;
Browser.isSupported           = Browser_isSupported;
Browser.getPlugInList         = Browser_getPlugInList;
Browser.getMimeTypeList       = Browser_getMimeTypeList;
Browser.isCookieEnabled       = Browser_isCookieEnabled;
Browser.toString              = Browser_toString;


/*
+ ---------------------------------------------------------------------------------+
| Object...: Environment
| Zweck....: Dient zur Abfrage der User-Umgebung
|
| Datum       Author            Bemerkung
| ----------  ----------------  ----------------------------------------------------
| 23.12.2002  G.Schulz (SCC)    Erstversion
|
+ ---------------------------------------------------------------------------------+
*/
function Environment() {
}
function Environment_getWidth() {
	return screen.width;
}
function Environment_getHeight() {
	return screen.height;
}
function Environment_screenAttributes() {
	var out = '';
	
	out += 'Width: ' + this.getWidth() + '; ';
	out += 'Height: ' + this.getHeight();
	return out;
}
function Environment_toString() {
}
new Environment();
Environment.getWidth             = Environment_getWidth;
Environment.getHeight            = Environment_getHeight;
Environment.screenAttributes     = Environment_screenAttributes;
Environment.toString             = Environment_toString;

