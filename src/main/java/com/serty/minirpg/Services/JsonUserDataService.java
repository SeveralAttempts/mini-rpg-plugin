/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.serty.minirpg.Services;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serty.minirpg.Entities.MiniRpgUserData;

/**
 *
 * @author pimgg
 */
@Singleton
public class JsonUserDataService {
    public final String userDataFileName = "minirpguserdata.json";

    @Inject
    public JsonUserDataService() {
    }

    public boolean initializeUserData(File dataFolder) {
        if (!dataFolder.exists()) {
            var dataFolderWasMade = dataFolder.mkdirs();

            if (!dataFolderWasMade) return false;
        }

        File userDataFile = new File(dataFolder, userDataFileName);

        try {
            userDataFile.createNewFile();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public boolean writeJsonConfig(ArrayList<MiniRpgUserData> data, File dataFolder) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            File out = new File(dataFolder, userDataFileName);
            mapper.writerWithDefaultPrettyPrinter().writeValue(out, data);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public ArrayList<MiniRpgUserData> readJsonConfig(File dataFolder) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            ArrayList<MiniRpgUserData> userDataList = mapper.readValue(
                new File(dataFolder, userDataFileName),
                new TypeReference<ArrayList<MiniRpgUserData>>() {});
                return userDataList;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
