# Stardist_Notebooks
Jupyter notebooks for training models

* **Developed for:** Orion image analysis facility
* **Date:** January 2023
* **Software:** Jupyter notebooks

### Conda envs (to create conda env  : conda create -n myenv --file package-list.txt)
  stardist.txt env for training notebook
  stardist_export_TF1.txt env for saving model to tensorflow 1 version compatible with stardis
  
### Notebooks
training : (run the script in stardist conda env) 
  data.ipynb loading images training and validations
  training.ipynb training model
  prediction.ipynb test model
  
Export model (run the script in stardist_export_TF1 conda env) 
  exportTf1Model.py to use run the script in stardist_export_TF1 conda env
  
### Models
  pmls2 model for pml
  fociRNA_1.0/1 model trained on RNA scope (Selimi team)
  fociRNA_1.2 model trained with a mix RNA scope / PML 

### Fiji plugin
  stardist_Foci_extport  Export foci detection to roi manager file
  
### Dependencies
* **Stardist** conda environment

### Version history

Version 1 released on January 5, 2023.

