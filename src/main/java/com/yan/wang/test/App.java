package com.yan.wang.test;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import de.slackspace.openkeepass.KeePassDatabase;
import de.slackspace.openkeepass.domain.Entry;
import de.slackspace.openkeepass.domain.KeePassFile;

import java.io.*;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws Exception {

        System.out.println( "Hello World!" );

        DbxRequestConfig config = new DbxRequestConfig("yanwangch_test123/1.0", "en_US");
        DbxClientV2 client = new DbxClientV2(config, "08dbXuYWlPEAAAAAAAAQcPO7tobYMZXNnJjPnDzSo7U6ZZxcx4eZ4HfIlQhBAj5y");

        testDropboxClientConfig(client);

        DbxDownloader<FileMetadata> dbxDownloader = client.files().download("/Yan Wang/YW_Private_Keys_v1.kdbx");
        InputStream inputStream = dbxDownloader.getInputStream();

//        OutputStream outputStream = new FileOutputStream(new File("YW_Private_Keys_v1.kdbx"));
//
//        byte[] buffer = new byte[1024];
//        int bytesRead;
//        //read from is to buffer
//        while((bytesRead = inputStream.read(buffer)) !=-1) {
//            outputStream.write(buffer, 0, bytesRead);
//        }
//            inputStream.close();
//        //flush OutputStream to write any buffered data to file
//            outputStream.flush();
//            outputStream.close();
//
//        System.out.println(dbxDownloader.getResult().getSize());


        // Open Database
        KeePassFile database = KeePassDatabase.getInstance(inputStream).openDatabase("ouafahwafa79");

        // Retrieve all entries
        List<Entry> entries = database.getEntries();

        for (Entry entry : entries) {
            System.out.println(entry.getPassword());
            System.out.println(entry.getTitle());
            System.out.println("*******************************");
        }
        System.out.println(entries.size());
    }

    private static void testDropboxClientConfig(DbxClientV2 client) throws DbxException {
        // Get current account info
        FullAccount account = client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());

        // Get files and folder metadata from Dropbox root directory
        ListFolderResult result = client.files().listFolder("");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
                System.out.println(metadata.getPathDisplay());
                System.out.println(metadata.getName());
                System.out.println(metadata.getParentSharedFolderId());
                System.out.println("******************************");
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }
    }
}
