// Alvis
//
// Alignment Diagrams in LaTeX and SVG
//
// Copyright 2018 Richard Leggett, Samuel Martin
// samuel.martin@earlham.ac.uk
// 
// This is free software, supplied without warranty.

package Alvis.Diagrams;

import Alvis.AlignmentFilters.OverlapFilter;
import Alvis.Drawers.SVGDrawer;
import Alvis.Drawers.Drawer;
import Alvis.AlignmentFiles.AlignmentFile;
import Alvis.AlignmentFiles.Alignment;
import Alvis.AlignmentFiles.DetailedAlignmentFile;
import java.io.File;
import java.util.ArrayList;
import java.util.*;

import Alvis.DiagramOptions;

/**
 *
 * @author martins
 */
public class GenomeCoverageDiagram 
{
    private DiagramOptions m_options;
    private Drawer m_drawer;
    private GenomeCoverageImage m_genomeCoverageImage;
    
    public GenomeCoverageDiagram(DiagramOptions options) 
    {
        m_options = options;
    }
    
    public void makeBitmapsFromFile(AlignmentFile alignmentFile) 
    { 
        String outputDirectory = m_options.getOutputDirectory();
        File imagesDir = new File(outputDirectory+"/images");
        imagesDir.mkdir();

        String filename = "images/genome_coverage_image.png";
        m_genomeCoverageImage = new GenomeCoverageImage(m_options, filename);
        
        //TODO: write filter for overlaps so we don't get extra coverage.
        // NOOOOOOOOOO!!!!
        if(alignmentFile instanceof DetailedAlignmentFile)
        {
            ((DetailedAlignmentFile)alignmentFile).filterAlignments(new OverlapFilter());
        }
        
        for(int i = 0; i < alignmentFile.getNumberOfAlignments(); ++i)
        {
            Alignment a = alignmentFile.getAlignment(i);
            m_genomeCoverageImage.addAlignment(a);
        }

        m_genomeCoverageImage.saveImageFile();
    }
    
    public void writeOutputFile() 
    {
        double scale = 0.1;
        String filename = m_options.getOutputFilePath() + "_genomeCoverageDiagram";
        int height = (int)(m_genomeCoverageImage.getHeight() * scale);
        int width = (int)(m_genomeCoverageImage.getWidth() * scale);
        if(m_options.getOutputFormat().equals("tex"))
        {
            System.out.println("TeX output not currently supported for Genome Coverage diagrams.");
            return;
        }
        else
        {
            m_drawer = new SVGDrawer(filename, false, 1, height + 100, 2 * width, 100, 12);
        }
        
        
        m_drawer.openFile();
        String imageFilename = m_genomeCoverageImage.getFilename();
        m_drawer.openPicture(0.1, 0.1);
        int coverage = m_genomeCoverageImage.getMaxCoverage();
        m_drawer.drawScale(m_genomeCoverageImage.getScale(), 10, 10, coverage);
        double yStart = 70;
        double xStart = width/4;
        m_drawer.drawImage(xStart, yStart, width, height, imageFilename, "");
        
        HashMap<String, int[]> coverages = m_genomeCoverageImage.getCoverages();
        Object[] targetNames = coverages.keySet().toArray();
        Arrays.sort(targetNames);
        int totalRows = 0;
        for(Object key: targetNames)
        {
            int length = coverages.get(key.toString()).length;
            int numRows = (length / m_genomeCoverageImage.getWidth() ) + 1;
            int targetSize = m_genomeCoverageImage.getTargetSize(key.toString());
            double y = (totalRows + (numRows/2));
            m_drawer.drawText(xStart - 10, yStart + height - totalRows, "0", Drawer.Anchor.ANCHOR_RIGHT, Drawer.Align.ALIGN_RIGHT, "black");
            m_drawer.drawText(xStart + 10 + width, yStart + height - totalRows - numRows, Integer.toString(targetSize), Drawer.Anchor.ANCHOR_LEFT, Drawer.Align.ALIGN_LEFT, "black");
            m_drawer.drawText(xStart - 30, yStart + height - y, key.toString(), Drawer.Anchor.ANCHOR_RIGHT, Drawer.Align.ALIGN_RIGHT, "black");
            totalRows += numRows;
            totalRows += m_genomeCoverageImage.getRowsBetweenTargets();
        }
        
        int basesPerRow = m_options.getBinSize() * m_genomeCoverageImage.getWidth();
        m_drawer.drawText(xStart + width/2, 10, "Each row represents " + Integer.toString(basesPerRow) + " nt", Drawer.Anchor.ANCHOR_MIDDLE, Drawer.Align.ALIGN_MIDDLE, "black");
        
        m_drawer.closePicture();
        m_drawer.closeFile();
    }
}
