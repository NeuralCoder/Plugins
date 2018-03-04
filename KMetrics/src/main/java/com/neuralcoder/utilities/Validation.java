package com.neuralcoder.utilities;

public class Validation {
	
	/**
	 * @param fileName
	 * @return TRUE if the @fileName extension is ".c", otherwise return FALSE
	 */
	public boolean findExtension(String fileName) {
		boolean ok = false;
		String extension = "";
		if (null != fileName && fileName.length() > 2) {
			fileName = removeEmptySpace(fileName);
			int len = fileName.length();
			if (len > 2) {
				extension = fileName.substring(len - 2, len);
			} else {
				ok = false;
			}
			if (".c".equals(extension)) {
				ok = true;
			}
		}
		return ok;
	}
	
	/**
	 * @param text
	 * @return remove empty spaces from @text, if exists - ex ".c "
	 */
	public String removeEmptySpace(String text) {
		int len = text.length();
		if (len > 2 && text.substring(len - 1, len).equals(" ")) {
			text = text.substring(0, len - 1);
			len = len - 1;
		}
		return text;
	}
}
