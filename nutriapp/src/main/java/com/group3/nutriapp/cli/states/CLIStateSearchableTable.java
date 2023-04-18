package com.group3.nutriapp.cli.states;

import java.util.ArrayList;
import java.util.Arrays;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.model.Food;
import com.group3.nutriapp.model.Ingredient;

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

    /**
     * Whether or not this table is searchable.
     */
    private boolean isSearchable;

    /**
     * Whether or not the table is selectable.
     */
    private boolean isSelectable;

    /**
     * Array to store user selections in.
     */
    private ArrayList<Integer> selections;

    public CLIStateSearchableTable(CLI owner, String title, String[] headers, String[][] entries, boolean isSearchable, ArrayList<Integer> selections) { 
        super(owner, title); 
        this.headers = headers;
        this.entries = entries;
        this.sourceEntries = entries;
        this.isSearchable = isSearchable;

        if (selections != null) {
            this.selections = selections;
            this.isSelectable = true;
        }

        this.calculateTableWidthAndSizing();
    }

    /**
     * Pushes a searchable food table state onto the stack using specified array of foodstuff.
     * @param cli CLI instance
     * @param name The name of the table
     * @param food The array of foodstuff
     * @param isIngredientArray Whether or not this array contains ingredients
     * @param selections List to store selections in if necessary.
     */
    public static void pushFoodSearchState(CLI cli, String name, Food[] food, boolean isIngredientArray, ArrayList<Integer> selections) {
        // A bit nasty to do it like this, but I don't see any reason to have to copy/paste the table generation logic.
        // Ingredients have stock, the rest of the foodstuff does not, other than that all properties are the same.
        String[] headers;
        if (isIngredientArray)
            headers = new String[] { "Name", "Calories(Kcal)", "Protein(g)", "Carbs(g)", "Fat(g)", "Fiber(g)", "Stock" };
        else
            headers = new String[] { "Name", "Calories(Kcal)", "Protein(g)", "Carbs(g)", "Fat(g)", "Fiber(g)" };
        String[][] values = new String[food.length][];
        for (int i = 0; i < food.length; i++) {
            Food item = food[i];

            // Are we missing fat and fiber?
            values[i] = new String[] {
                item.getName(),
                Double.toString(item.getCalories()),
                Double.toString(item.getProtein()),
                Double.toString(item.getCarbs()),
                Double.toString(item.getFat()),
                Double.toString(item.getFiber()),
                // Only ingredients have stock
                isIngredientArray ? Integer.toString(((Ingredient) item).getStockCount()) : ""
            };
        }

        cli.push(new CLIStateSearchableTable(
            cli, 
            name, 
            headers, 
            values,
            true,
            selections
        ));
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

            // Add extra spacing for index if we're in selectable state
            if (i == 0 && this.isSelectable)
                this.sizing[i] += 4;

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
     * @param selectionIndex Selection index to show, -1 if not applicable.
     */
    private void showLineSpaced(String[] row, int selectionIndex) {
        // Pad the string builder buffer with space characters.
        StringBuilder sb = new StringBuilder(this.totalSize);
        for (int i = 0; i < this.totalSize; i++) sb.append(" ");

        // Insert each column at its specified offset according
        // to the sizing values pre-computed.
        int offset = 0;
        int col = this.headers.length;
        for (int i = 0; i < col; ++i) {
            String value = row[i];
            
            // Show selection index before first entry
            if (i == 0 && selectionIndex != -1)
                value = String.format("%2d. %s", selectionIndex, value);

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
        
        this.showLineSpaced(this.headers, -1);
        this.showDivider(false);

        int count = 0;
        for (int i = start; i < end; i++, count++)
            this.showLineSpaced(this.entries[i], isSelectable ? (i - start) : -1);

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
        boolean canGoNext = PAGE_SIZE < this.entries.length;
        int numPages = (this.entries.length - 1) / PAGE_SIZE;

        if (this.isSearchable) {
            // Show the current search filter on the menu option
            String searchText = "Search";
            if (!this.lastSearch.isEmpty())
                searchText += ": " + this.lastSearch;

            this.addOption(searchText, () -> {
                this.lastSearch = this.getInput("Enter a search query");
                this.doSearch();
            });
        }

        // Show next option only if there's enough data in the table
        if (canGoNext) {
            // Show what page index we're on (1-based) in the menu option
            String nextText = String.format("Next (%d/%d)", this.pageIndex + 1, numPages);
            this.addOption(nextText, () -> {
                // Loop back around if we get to the last page
                this.pageIndex = (this.pageIndex + 1) % numPages;
            });
        }

        if (this.isSelectable) {
            this.addOption("Select", () -> {
                int index = getOptionIndex();
                if (index == -1) return;

                // Make sure we take into account pagination and filters.
                int start = this.pageIndex * PAGE_SIZE;
                int end = Math.min(start + PAGE_SIZE, this.entries.length);
                index += start;

                // Print an error if we went out of bounds
                if (index >= end) {
                    this.showError("Invalid index!");
                    return;
                }

                // Map the filtered index to the source index
                int sourceIndex = Arrays.asList(this.sourceEntries).indexOf(this.entries[index]);

                if (this.selections.indexOf(sourceIndex) != -1)
                    this.showError("Item was already selected!");
                else {
                    this.selections.add(sourceIndex);
                    this.showMessage("Successfulyl added item to selections!");
                }
                
            });
        }

        this.addOptionDivider();
        this.addBackOption();
    }

    @Override public void run() {
        this.showTable();
        this.showOptions();
    }
    
}
