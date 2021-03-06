// Alvis
//
// Alignment Diagrams in LaTeX and SVG
//
// Copyright 2018 Richard Leggett, Samuel Martin
// samuel.martin@earlham.ac.uk
// 
// This is free software, supplied without warranty.

package Alvis.AlignmentFilters;

import Alvis.AlignmentFiles.DetailedAlignment;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author martins
 */
public class OverlapFilter implements AlignmentFilter
{
    
    public ArrayList<DetailedAlignment> filterAlignments(ArrayList<DetailedAlignment> alignments)
    {
        // sort into query
        Collections.sort(alignments, DetailedAlignment.compareByQueryName);
        ArrayList<DetailedAlignment> filteredAlignments = new ArrayList();
        
        // group alignments into arrays by query name
        int i = 0;
        while(i < alignments.size())
        {
            String lastQueryName = alignments.get(i).getQueryName();
            ArrayList<DetailedAlignment> alignmentsForQuery = new ArrayList();
            alignmentsForQuery.add(alignments.get(i));
            i++;
            while(i < alignments.size() && alignments.get(i).getQueryName().equals(lastQueryName))
            {
                alignmentsForQuery.add(alignments.get(i));
                i++;
            }
        
            // identify those alignments for this query that don't overlap
            filteredAlignments.addAll(findNonOverlapping(alignmentsForQuery));
        }
        
        return filteredAlignments;
    }   

    private ArrayList<DetailedAlignment> findNonOverlapping(ArrayList<DetailedAlignment> alignments)
    {
        //find the longest alignment
        Collections.sort(alignments, DetailedAlignment.compareByQueryAlignmentLength);
        
        DetailedAlignment bestAlignment = alignments.get(0);
        ArrayList<DetailedAlignment> filteredAlignments = new ArrayList();
        filteredAlignments.add(bestAlignment);
        for(DetailedAlignment alignment : alignments)
        {
            if(alignment == bestAlignment)
            {
                continue;
            }
            boolean add = true;
            for(DetailedAlignment alignment2 : filteredAlignments)
            {              
                if(doAlignmentsOverlap(alignment, alignment2))
                { 
                    add = false;
                    break;
                }
            }
            
            if(add)
            {
                filteredAlignments.add(alignment);
            }
//            else
//            {
//                System.out.print("Removing alignments from query " + alignment.getQueryName() + "\t" + 
//                        Integer.toString(alignment.getQueryStart()) + "\t" + 
//                        Integer.toString(alignment.getQueryEnd()) + "\n" );
//            }
        }     
        return filteredAlignments;
    }    
    
    private boolean doAlignmentsOverlap(DetailedAlignment alignment1, DetailedAlignment alignment2)
    {
        // start or end of one alignment is contained in the other
        if( (alignment1.getQueryEnd() > alignment2.getQueryStart() && alignment1.getQueryEnd() <= alignment2.getQueryEnd()) ||
            (alignment1.getQueryStart() < alignment2.getQueryEnd() && alignment1.getQueryStart() >= alignment2.getQueryStart())  )
        {
            return true;
        }
        // one alignment is entirely contained in the other
        if( (alignment1.getQueryStart() < alignment2.getQueryStart() && alignment1.getQueryEnd() > alignment2.getQueryEnd()) || 
            (alignment2.getQueryStart() < alignment1.getQueryStart() && alignment2.getQueryEnd() > alignment1.getQueryEnd())  )
        {
            return true;
        }
        return false;
    }
}
