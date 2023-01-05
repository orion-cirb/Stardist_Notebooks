package Stardist_Foci_Export_Tools;



import fiji.util.gui.GenericDialogPlus;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;
import mcib3d.geom.Object3D;
import mcib3d.geom.Objects3DPopulation;
import mcib3d.image3d.ImageHandler;
import org.apache.commons.io.FilenameUtils;
import ij.plugin.frame.RoiManager;
import java.io.FilenameFilter;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.RealType;
import StardistOrion.StarDist2D;


/**
 *
 * @author phm
 */

public class Foci_Processing {
    
// Images paths
    public String imgFolder = "";
    public String roiFolder = "";
    
// Stardist parameters
    private File modelsPath = new File(IJ.getDirectory("imagej")+File.separator+"models");
    private String stardistFociModel = "pmls2.zip";
    private Object syncObject = new Object();
    private String stardistOutput = "ROI Manager";
    private double stardistPercentileBottom = 0.2;
    private double stardistPercentileTop = 99.8;
    private double stardistFociProbThresh = 0.50;
    private double stardistFociOverlayThresh = 0.25;   
    

    public final ImageIcon icon = new ImageIcon(this.getClass().getResource("/Orion_icon.png"));
    
     /**
     * Return StarDist models present in Fiji models folder
     */
    public String[] checkStarDistModels() {
        FilenameFilter filter = (dir, name) -> name.endsWith(".zip");
        String[] modelList = modelsPath.list(filter);
        if (modelList.length == 0) {
            IJ.showMessage("Error no StarDist model found, please add it in Fiji models folder");
        }
        return modelList;
    }
    
    
    
    /**
     * Dialog
     */
    public boolean dialog() {
        String[] models = checkStarDistModels();
        GenericDialogPlus gd = new GenericDialogPlus("Parameters");
        gd.setInsets(0, 120, 0);
        gd.addImage(icon);
        gd.addDirectoryField("Images folder : ", "");
        gd.addDirectoryField("Rois folder   : ", "");
        gd.addMessage("Stardist parameters", Font.getFont("Monospace"), Color.blue);
        gd.addChoice("Model : ", models, models[0]);
        gd.addNumericField("Probability threshold : ", stardistFociProbThresh);
        gd.addNumericField("Overlay threshold     : ", stardistFociOverlayThresh);
        gd.showDialog();
        if (gd.wasCanceled())
            return(true);
        imgFolder = gd.getNextString();
        roiFolder = gd.getNextString();
        stardistFociModel = gd.getNextChoice();
        stardistFociProbThresh = gd.getNextNumber();
        stardistFociOverlayThresh = gd.getNextNumber();
        return(false);
    } 
    
    
    
   /**
     * Find images in folder
     */
    public ArrayList findImages(String imagesFolder, String imageExt) {
        File inDir = new File(imagesFolder);
        String[] files = inDir.list();
        if (files == null) {
            System.out.println("No Image found in "+imagesFolder);
            return null;
        }
        ArrayList<String> images = new ArrayList();
        for (String f : files) {
            // Find images with extension
            String fileExt = FilenameUtils.getExtension(f);
            if (fileExt.equalsIgnoreCase(imageExt))
                images.add(imagesFolder + File.separator + f);
        }
        Collections.sort(images);
        return(images);
    }
        
    
    /**
     *
     * @param img
     */
    public void closeImages(ImagePlus img) {
        img.flush();
        img.close();
    }

    
  
    /** Stardist dots detection
     * return RoiManager
    */
    public void stardistRoisExports(ImagePlus img, String imgName){
        // StarDist
        File starDistModelFile = new File(modelsPath+File.separator+stardistFociModel);
        StarDist2D star = new StarDist2D(syncObject, starDistModelFile);
        star.loadInput(img);
        star.setParams(stardistPercentileBottom, stardistPercentileTop, stardistFociProbThresh, stardistFociOverlayThresh, stardistOutput);
        star.run();
        closeImages(img);
        RoiManager rm = RoiManager.getInstance();
        if (rm.getCount() > 0)
            rm.save(roiFolder+File.separator+imgName);
        rm.close();
    }
    
   
}
