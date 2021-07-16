/*
 * Copyright (c) 2021  Dinusha Jayawardena. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package controller;

import javafx.scene.control.TextArea;

import java.io.*;

public class AboutFormController {
    public TextArea txtNote;

    public void initialize() throws IOException {
        File fileReader = new File("About.txt");
       // System.out.println("Exists: " + fileReader.exists());

        try (FileReader fr = new FileReader(fileReader);
             BufferedReader br = new BufferedReader(fr)) {

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = br.readLine()) != null){
                sb.append(line + "\n");
            }
            //System.out.println(sb.toString());

            txtNote.setText(sb.toString());
        }
    }
}
