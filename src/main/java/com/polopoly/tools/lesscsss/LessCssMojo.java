/*
 * Copyright (c) Polopoly AB (publ).
 * Dual licensed under the MIT or GPL Version 2 licenses.
 */
package com.polopoly.tools.lesscsss;

import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.shared.model.fileset.FileSet;

/**
 * Generate css from less files.
 * 
 * @goal lesscss
 * @phase process-resources
 */
public class LessCssMojo
    extends AbstractMojo
{

    /**
     * Lesscss source base directory (where the less files are put).
     *
     * @parameter expression="${lesscss.sourceDir}" default-value="${project.basedir}/src/main/lesscss"
     * @required
     * @since 1.0.0
     */
    private String sourceDir;

    /**
     * Lesscss source filenames exclude patterns. The plugin will scan the sourceDir
     * for .less files and process them unless it matches any of the exclude patterns (ant like 
     * patterns relative to sourceDir). This parameter defaults to 'include/**'.
     *
     * @parameter
     * @since 1.0.0
     */
    private String[] excludes;

    /**
     * Target directory for the generated css files.
     *
     * @parameter expression="${lesscss.targetDir}"
     *            default-value="${project.build.directory}/${project.build.finalName}"
     * @required
     * @since 1.0.0
     */
    private String targetDir;
    
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        LessCssProcessor processor = new LessCssProcessor(sourceDir, targetDir, excludes);
        processor.process();           
    }

    /*
     * Plugin 'interface' for content scan.
     *
     * inputFolders  = files that should cause this plugin to run again
     * outputFolders = files that should be ignored since we are already watching the input folders
     */

    public List<FileSet> getInputFolders() {
        FileSet sourceFiles = new FileSet();
        sourceFiles.setDirectory(sourceDir);
        return Collections.singletonList(sourceFiles);
    }

    public List<FileSet> getOutputFolders() {
        FileSet targetFiles = new FileSet();
        targetFiles.setDirectory(targetDir);
        targetFiles.addInclude("**/*.css");
        return Collections.singletonList(targetFiles);
    }
}
