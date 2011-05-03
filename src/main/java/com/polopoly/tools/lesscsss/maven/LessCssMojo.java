package com.polopoly.tools.lesscsss.maven;

import static java.lang.String.format;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.asual.lesscss.LessEngine;

/**
 * @goal lesscss
 * @phase process-resources
 */
public class LessCssMojo
    extends AbstractMojo
{

    /**
     * LESS CSS source base directory.
     * 
     * @parameter expression="${lesscss.sourceDir}" default-value="${project.basedir}/src/main/lesscss"
     * @required
     */
    private String sourceDir;
    
    /**
     * LESS CSS source filenames list. Relative to base directory.
     * 
     * @parameter
     * @required
     */
    private List<String> lessCssFiles = new ArrayList<String>();
    
    /**
     * 
     * 
     */
    
    /**
     * Webapp target directory.
     * 
     * @parameter expression="${lesscss.targetDir}"
     *            default-value="${project.build.directory}/${project.build.finalName}"
     * @required
     */
    private String targetDir;
    
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        LessEngine lessEngine = new LessEngine();
        for (String source : lessCssFiles) {
            File sourceFile = new File(sourceDir, source);
            if (!sourceFile.exists()) {
                throw new MojoExecutionException(format("Could not find source file, tried %s and %s.",
                                                        source, sourceFile));
            }
            File targetFile = new File(targetDir, source.replace(".less", ".css"));
            targetFile.getParentFile().mkdirs();
            try {
                format("Creating %s from %s.", targetFile, sourceFile);
                lessEngine.compile(sourceFile, targetFile);
            }
            catch (Exception e) {
                throw new MojoExecutionException("Failed to compile " + sourceFile + " to " + targetFile + ".", e);
            }
        }
    }

}
