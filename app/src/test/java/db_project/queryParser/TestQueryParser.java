package db_project.queryParser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import db_project.db.queryUtils.TestParser;

class TestQueryParser {

    @Test
    void testSelect() {
        assertTrue(TestParser.testSelect(TestParser.getParser()));
    }
    
    @Test
    void testInsert() {
        assertTrue(TestParser.testInsert(TestParser.getParser()));
    }
}