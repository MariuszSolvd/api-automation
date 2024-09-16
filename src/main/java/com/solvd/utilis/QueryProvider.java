package com.solvd.utilis;

public final class QueryProvider {
    public static final String GET_USER_BY_ID = "{\"query\": \"query User{ user(id: %s) { id name email gender status }}\"}";
    public static final String CREATE_USER = "{\"query\": \"mutation { createUser(input: { name: \\\"%s\\\", email: \\\"%s\\\", gender: \\\"%s\\\", status: \\\"%s\\\" }) { user { id name email gender status } } }\"}";
}
