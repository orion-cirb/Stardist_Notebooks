/*
 * Find dots with stardist model
 * Export labelled dost to rois 
 * Author Philippe Mailly
 */

import Stardist_Foci_Export_Tools.Foci_Processing;
import ij.*;
import ij.plugin.PlugIn;
import ij.plugin.frame.RoiManager;
import java.io.File;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;


public class Foci_Export implements PlugIn {

    
    private Stardist_Foci_Export_Tools.Foci_Processing proc = new Foci_Processing();

      

    /**
     * 
     * @param arg
     */
    @Override
    public void run(String arg) {
                
        boolean dialog = proc.dialog();
        if (dialog)
            return;
        if (proc.imgFolder == null || proc.roiFolder == null) {
            return;
        }
        String fileExt = "tif";
        ArrayList<String> imageFiles = proc.findImages(proc.imgFolder, fileExt);
        if (imageFiles == null) {
            IJ.showMessage("Error", "No images found");
            return;
        }

        
        // Read images
        for (String file :  imageFiles) {
            String rootName = FilenameUtils.getBaseName(file);
            // Open image
            ImagePlus img = IJ.openImage(file);
            proc.stardistRoisExports(img, rootName+".zip");
        }
        IJ.showStatus("Process done");
       
    }
}