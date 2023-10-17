package org.conflito.matcher;

import java.io.File;

public abstract class AbstractTestMatcher {

	protected static final String RELATIVE_SRC_RESOURCES_DIR_PATH = "src" + File.separator + "main" + File.separator + "resources";

  protected static final String RELATIVE_TEST_RESOURCES_DIR_PATH = "src" + File.separator + "test" + File.separator + "resources";

  protected static final String RELATIVE_SRC_DIR_PATH = "src" + File.separator + "main" + File.separator + "java";

  protected static final String CONFIG_FILE_NAME = "config.properties";

  protected static final String BASE_BRANCH_NAME = "base";

  protected static final String BRANCH_1_NAME = "branch1";

  protected static final String BRANCH_2_NAME = "branch2";

  protected static final String buildPath(final String... args) {
    return String.join(File.separator, args);
  }

  protected static final boolean checkIfFilesExist(String[] paths) {
    for (String path : paths) {
      if (path != null) {
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
          return false;
        }
      }
    }
    return true;
  }

}
