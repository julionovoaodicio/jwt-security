package com.jakublesko.jwtsecurity.documentdb.sample.dao;

import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;

public class DocumentClientFactory {
    private static final String HOST = "https://db001core.documents.azure.com:443/";
    private static final String MASTER_KEY = "QQOJvrZxHMWKDLMSTDOJqtHF2PwsLZPmUaZVBzYhfWkn56t1dnozPQZirHeCJikqvfTkFVENwAAdYWiHDTXZgQ==";

    private static DocumentClient documentClient;

    public static DocumentClient getDocumentClient() {
        if (documentClient == null) {
            documentClient = new DocumentClient(HOST, MASTER_KEY,
                    ConnectionPolicy.GetDefault(), ConsistencyLevel.Session);
        }

        return documentClient;
    }

}
