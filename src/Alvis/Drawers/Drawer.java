// Alvis
//
// Alignment Diagrams in LaTeX and SVG
//
// Copyright 2018 Richard Leggett, Samuel Martin
// samuel.martin@earlham.ac.uk
// 
// This is free software, supplied without warranty.

package Alvis.Drawers;

import Alvis.Diagrams.CoverageMapImage;
import Alvis.Diagrams.HeatMapScale;

/**
 *
 * @author martins
 */

public interface Drawer 
{
    public enum Anchor
    {
        ANCHOR_LEFT,
        ANCHOR_MIDDLE,
        ANCHOR_RIGHT 
    }
    
    public enum Align
    {
        ALIGN_LEFT,
        ALIGN_MIDDLE,
        ALIGN_RIGHT
    }
    
    abstract void openFile();
    abstract void closeFile();
    abstract void openPicture(double x, double y);
    abstract void closePicture();
    abstract void newPage();
    abstract void drawLine(double x1, double y1, double x2, double y2, String colour, boolean dashed);
    abstract void drawCurve(double startx, double starty, double endx, double endy, double controlx1, double controly1, double controlx2, double controly2);
    abstract void drawRectangle(double x, double y, double width, double height, String borderColour);
    abstract void drawFilledRectangle(double x, double y, double width, double height, String fillColour, String borderColour);
    abstract void drawText(double x, double y, String text, Anchor anchor, Align align, String colour);
    abstract void drawTextWithMaxWidth(double x, double y, String text, Anchor anchor, Align align, String colour, int maxWidth);
    abstract void drawTextRotated(double x, double y, String text, int angle, Anchor anchor);
    abstract void drawImage(double x, double y, double width, double height, String filename, String optionsString);
    abstract void defineColour(String name, int red, int green, int blue);
    abstract void drawVerticalGap(int y);
    abstract void drawHorizontalGap(int x);
    abstract void drawNewline();
    abstract int getPageHeight();
    
    // Detailed Alignment Diagram
    abstract int  getMaxAlignmentsPerPage(); 
    abstract void drawAlignment(double x, double y, double width, double height, String fillColour, String borderColour, int fillLeftPC, int fillRightPC);
    abstract void drawAlignment(double x, double y, double width, double height, String fillColour, String borderColour, int fillLeftPC, int fillRightPC, String alignmentID);
    abstract void drawKeyContig(double x, double y, double width, double height, String colour, String name);
    
    // Coverage Map Diagram
    abstract void drawScale(HeatMapScale heatMapScale, double x, double y, int coverage);
    abstract void drawCoverageMap(CoverageMapImage coverageMap, double x, double y);
    abstract void drawCoverageLong(CoverageMapImage coverageMap, double x, double y, double imageWidth, double imageHeight, int num_dividers);
    
}
