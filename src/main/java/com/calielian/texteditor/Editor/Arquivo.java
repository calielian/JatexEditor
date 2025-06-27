package com.calielian.texteditor.Editor;

import java.io.File;
import java.io.IOException;

public class Arquivo {
    private static File file;

    public static void criarNovoArquivo(String nome, String caminho) {
        file = new File(caminho + nome);

        try {
            if (file.createNewFile()) return file;
            else {
                
            }

        } catch (IOException e) {
            file = null;
            return file;
        }
    }

}
