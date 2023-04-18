package com.group3.nutriapp.cli.states;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

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
     * The function that gets run when an entry is selected.
     */
    private Consumer<Integer> selectionCallback;

    public CLIStateSearchableTable(CLI owner, String title, String[] headers, String[][] entries, boolean isSearchable, Consumer<Integer> selectionCallback) { 
        super(owner, title); 
        this.headers = headers;
        this.entries = entries;
        sourceEntries = entries;
        this.isSearchable = isSearchable;

        if (selectionCallback != null) {
            this.selectionCallback = selectionCallback;
            isSelectable = true;
        }

        calculateTableWidthAndSizing();
    }

    /**
     * Pushes a searchable food table state onto the stack using specified array of foodstuff.
     * @param cli CLI instance
     * @param name The name of the table
     * @param food The array of foodstuff
     * @param isIngredientArray Whether or not this array contains ingredients
     * @param selections List to store selections in if necessary.
     */
    public static void pushFoodSearchState(CLI cli, String name, Food[] food, boolean isIngredientArray, Consumer<Integer> selectionCallback) {
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

        // Push the searchable table state onto the stack.
        cli.push(new CLIStateSearchableTable(
            cli, 
            name, 
            headers, 
            values,
            true,
            selectionCallback
        ));
    }

    /**
     * Calculates the necessary width and sizing of each column
     * to fit all the data in the table cleanly in the frame.
     */
    private void calculateTableWidthAndSizing() {
        int start = pageIndex * PAGE_SIZE;
        int end = start + PAGE_SIZE;
        // Make sure we don't read more than exists
        if (end > entries.length)
            end = entries.length;

        // Calculate the sizing of each column in the table
        sizing = new int[headers.length];

        // Find the longest strings in each column
        for (int i = start; i < entries.length; i++) {
            for (int j = 0; j < headers.length; ++j) {
                int length = entries[i][j].length();
                if (length > sizing[j])
                    sizing[j] = length;
            }
        }

        totalSize = 0;
        for (int i = 0; i < headers.length; ++i) {

            // If the header is bigger than the size of any of
            // the entries, fit it
            String header = headers[i];
            if (sizing[i] < header.length())
                sizing[i] = header.length();

            // Add extra spacing for index if we're in selectable state
            if (i == 0 && isSelectable)
                sizing[i] += 4;

            // 4 character spacing between each entry
            totalSize += (sizing[i] + 4);
        }

        // Allow 2 character spacing between each side
        int tableWidth = totalSize + 4;

        // Set the table width to make sure all data fits
        setTableWidth(tableWidth);
    }

    /**
     * Shows keys in a spaced line based on this table's sizing properties.
     * @param row Row data in the table
     * @param selectionIndex Selection index to show, -1 if not applicable.
     */
    private void showLineSpaced(String[] row, int selectionIndex) {
        // Pad the string builder buffer with space characters.
        StringBuilder sb = new StringBuilder(totalSize);
        for (int i = 0; i < totalSize; i++) sb.append(" ");

        // Insert each column at its specified offset according
        // to the sizing values pre-computed.
        int offset = 0;
        int col = headers.length;
        for (int i = 0; i < col; ++i) {
            String value = row[i];
            
            // Show selection index before first entry
            if (i == 0 && selectionIndex != -1)
                value = String.format("%2d. %s", selectionIndex, value);

            int size = sizing[i];
            sb.replace(offset, offset + value.length(), value);
            offset += (size + 4);
        }

        // Actually show the line on the screen
        showLine(sb.toString());
    }

    /**
     * Renders the table to the screen.
     */
    private void showTable() {
        int start = pageIndex * PAGE_SIZE;
        int end = start + PAGE_SIZE;
        // Make sure we don't read more than exists
        if (end > entries.length)
            end = entries.length;
        
        showLineSpaced(headers, -1);
        showDivider(false);

        int count = 0;
        for (int i = start; i < end; i++, count++)
            showLineSpaced(entries[i], isSelectable ? (i - start) : -1);

        // Show an empty line if the table is empty to make it look less awkward.
        if (count == 0)
            showLineCentered("");
        
        showDivider(false);
    }

    /**
     * Event that triggers when the user opts to search the table.
     * 
     * Filters the source entries based on the search query provided by the user.
     * 
     * If the search query is empty, restore the original list
     * Otherwise do a case insensitive search against the first column's data
     */
    private void onSearch() {
        lastSearch = getInput("Enter a search query");

        // Reset the page index since searching will almost always
        // change the order of the table.
        pageIndex = 0;

        // If the search result is empty, just restore the original table
        if (lastSearch.isEmpty()) {
            entries = sourceEntries;
            return;
        }

        ArrayList<String[]> filtered = new ArrayList<>(sourceEntries.length);
        
        // For now, we just do a case-insensitive filter against the first column
        String filter = lastSearch.toLowerCase();
        for (String[] entry : sourceEntries) {
            String value = entry[0];
            if (value.toLowerCase().contains(filter))
                filtered.add(entry);
        }

        // Store the filtered result into the table array
        entries = filtered.toArray(String[][]::new);
    }

    /**
     * Event that triggers when the user opts to select an element in the table.
     */
    private void onSelect() {
        int index = getOptionIndex();
        if (index == -1) return;

        // Make sure we take into account pagination and filters.
        int start = pageIndex * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, entries.length);
        index += start;

        // Print an error if we went out of bounds
        if (index >= end) {
            showError("Invalid index!");
            return;
        }

        // Map the filtered index to the source index
        int sourceIndex = Arrays.asList(sourceEntries).indexOf(entries[index]);

        // Pass selected index to callback
        selectionCallback.accept(sourceIndex);
        // Since we've selected an item, pop the state
        getOwner().pop();
    }

    /**
     * Shows the options available to the user
     */
    private void showOptions() {
        boolean canGoNext = PAGE_SIZE < entries.length;
        int numPages = (entries.length - 1) / PAGE_SIZE;

        // Allow the user to search the table if this table was created in such a state.
        if (isSearchable) {
            // Show the current search filter on the menu option
            String searchText = "Search";
            if (!lastSearch.isEmpty())
                searchText += ": " + lastSearch;
            
            addOption(searchText, this::onSearch);
        }

        // Show next option only if there's enough data in the table
        if (canGoNext) {
            // Show what page index we're on (1-based) in the menu option
            String nextText = String.format("Next (%d/%d)", pageIndex + 1, numPages);
            addOption(nextText, () -> {
                // Loop back around if we get to the last page
                pageIndex = (pageIndex + 1) % numPages;
            });
        }

        if (isSelectable)
            addOption("Select", this::onSelect);

        // Default functionality for going back to previous page
        addOptionDivider();
        addBackOption();
    }

    /**
     * Prints a table based on the data provided to the state.
     * Then provides the user with options to interface with said data.
     */
    @Override public void run() {
        showTable();
        showOptions();
    }
    
}
