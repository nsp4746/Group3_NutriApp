package com.group3.nutriapp.cli.states;

import java.util.ArrayList;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;

/**
 * @author Aidan Ruiz + Group 3
 * @description CLI state that presents data from a table in a paginated fashion.
 * @date 4/11/23
 */
public class CLIStateSearchableTable extends CLIState {
    /**
     * The max number of entries shown in each page of the table.
     */
    private static final int PAGE_SIZE = 16;

    /**
     * The last search the user inputted.
     */
    private String lastSearch = "";

    /**
     * The current offset into the table data in terms of page size.
     */
    private int pageIndex = 0;

    /**
     * Names of the columns in the table.
     */
    private String[] headers;

    /**
     * Original unfiltered table row data
     */
    private String[][] sourceEntries;

    /**
     * Currently rendered table row data
     */
    private String[][] entries;

    /**
     * The size of each column in the table
     */
    private int[] sizing;

    /**
     * Cached size of all columns cumulatively.
     */
    private int totalSize;

    public CLIStateSearchableTable(CLI owner, String title, String[] headers, String[][] entries) { 
        super(owner, title); 
        this.headers = headers;
        this.entries = entries;
        this.sourceEntries = entries;
        this.calculateTableWidthAndSizing();
    }

    /**
     * Calculates the necessary width and sizing of each column
     * to fit all the data in the table cleanly in the frame.
     */
    private void calculateTableWidthAndSizing() {
        int start = this.pageIndex * PAGE_SIZE;
        int end = start + PAGE_SIZE;
        // Make sure we don't read more than exists
        if (end > this.entries.length)
            end = this.entries.length;

        // Calculate the sizing of each column in the table
        this.sizing = new int[this.headers.length];

        // Find the longest strings in each column
        for (int i = start; i < this.entries.length; i++) {
            for (int j = 0; j < this.headers.length; ++j) {
                int length = this.entries[i][j].length();
                if (length > this.sizing[j])
                    this.sizing[j] = length;
            }
        }

        this.totalSize = 0;
        for (int i = 0; i < this.headers.length; ++i) {

            // If the header is bigger than the size of any of
            // the entries, fit it
            String header = this.headers[i];
            if (this.sizing[i] < header.length())
                this.sizing[i] = header.length();

            // 4 character spacing between each entry
            this.totalSize += (this.sizing[i] + 4);
        }

        // Allow 2 character spacing between each side
        int tableWidth = this.totalSize + 4;

        // Set the table width to make sure all data fits
        this.setTableWidth(tableWidth);
    }

    /**
     * Shows keys in a spaced line based on this table's sizing properties.
     * @param row Row data in the table
     */
    private void showLineSpaced(String[] row) {
        // Pad the string builder buffer with space characters.
        StringBuilder sb = new StringBuilder(this.totalSize);
        for (int i = 0; i < this.totalSize; i++) sb.append(" ");

        // Insert each column at its specified offset according
        // to the sizing values pre-computed.
        int offset = 0;
        for (int i = 0; i < row.length; ++i) {
            String value = row[i];
            int size = this.sizing[i];
            sb.replace(offset, offset + value.length(), value);
            offset += (size + 4);
        }

        // Actually show the line on the screen
        this.showLine(sb.toString());
    }

    /**
     * Renders the table to the screen.
     */
    private void showTable() {
        int start = this.pageIndex * PAGE_SIZE;
        int end = start + PAGE_SIZE;
        // Make sure we don't read more than exists
        if (end > this.entries.length)
            end = this.entries.length;
        
        this.showLineSpaced(this.headers);
        this.showDivider(false);

        int count = 0;
        for (int i = start; i < end; i++, count++)
            this.showLineSpaced(this.entries[i]);

        // Show an empty line if the table is empty to make it look less awkward.
        if (count == 0)
            this.showLineCentered("");
        
        this.showDivider(false);
    }

    private void doSearch() {
        this.pageIndex = 0;

        // If the search result is empty, just restore the original table
        if (this.lastSearch.isEmpty()) {
            this.entries = this.sourceEntries;
            return;
        }

        ArrayList<String[]> filtered = new ArrayList<>(this.sourceEntries.length);
        
        // For now, we just do a case-insensitive filter against the first column
        String filter = this.lastSearch.toLowerCase();
        for (String[] entry : this.sourceEntries) {
            String value = entry[0];
            if (value.toLowerCase().contains(filter))
                filtered.add(entry);
        }

        // Store the filtered result into the table array
        this.entries = filtered.toArray(String[][]::new);
    }

    /**
     * Shows the options available to the user
     */
    private void showOptions() {
        final int BACK = 0, SEARCH = 1, NEXT = 2;

        boolean canGoNext = PAGE_SIZE < this.entries.length;
        int numPages = (this.entries.length - 1) / PAGE_SIZE;

        // Show the current search filter on the menu option
        String searchText = "Search";
        if (!this.lastSearch.isEmpty())
            searchText += ": " + this.lastSearch;
        
        // Show next option only if there's enough data in the table
        if (canGoNext) {
            // Show what page index we're on (1-based) in the menu option
            String nextText = String.format("Next (%d/%d)", this.pageIndex + 1, numPages);

            this.showMenu(new String[] { "Back", searchText, nextText });
        }
        else
            this.showMenu(new String[] { "Back", searchText });

        int command = this.getOptionIndex();
        if (command == -1) return;

        if (command == SEARCH) {
            this.lastSearch = this.getInput("Enter a search query");
            this.doSearch();
        }
        else if (command == BACK)
            this.getOwner().pop();
        else if (command == NEXT && canGoNext) {
            // Loop back around if we get to the last page
            this.pageIndex = (this.pageIndex + 1) % numPages;
        } else 
            this.showError("Invalid command!");
    }

    @Override public void run() {
        this.showHeader();
        this.showTable();
        this.showOptions();
    }
    
}
