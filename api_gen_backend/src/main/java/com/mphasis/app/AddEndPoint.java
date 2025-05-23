package com.mphasis.app;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.app.model.ModfiyModel;
import com.mphasis.app.modfiy.ModifyController;


public class AddEndPoint {

    ModfiyModel modifyModel;
    private String savePath;   
    ObjectMapper objectMapper = new ObjectMapper();
    

    public AddEndPoint(JsonNode inputJson, String savePath) {
        
        this.savePath = savePath;
        try {
            modifyModel = objectMapper.treeToValue(inputJson, ModfiyModel.class);
            System.out.println("Parsed Table from JSON");
        } catch (Exception e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
            return;
        }
    }

    public void modify() throws Exception {

        if (modifyModel == null) {
            throw new Exception("ModifyModel is not initialized");
        }

        String ModelfilePath = savePath + "/model/" + modifyModel.getModelName() + "Model" + ".java";
        String ServicefilePath = savePath + "/service/" + modifyModel.getModelName() + "Service" + ".java";
        String ControllerfilePath = savePath + "/controller/" + modifyModel.getModelName() + "Controller" + ".java";
        String RepoFilePath = savePath + "/repository/" + modifyModel.getModelName() + "Repo" + ".java";
        
        if (modifyModel.getType().equals("basic")) {
            
            ModifyController modifyController = new ModifyController(modifyModel.getModelName());
            try{
                modifyController.modify(modifyModel.getRequirement(), ControllerfilePath);
            }
            catch (Exception e) {
                System.out.println("Error modifying controller: " + e.getMessage());
            }
        }
    }
}
