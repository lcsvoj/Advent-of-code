package main

import (
	"bufio"
	"fmt"
	"os"
	"unicode"
)

type Cell struct {
	value      rune
	row        int
	column     int
	checked    bool
	isolated   bool
	linkedCell map[string]Cell
}

func (c Cell) Print(s string) (toPrint string) {
	if s == "xy" {
		toPrint = fmt.Sprintf("(%d, %d)", c.row, c.column)
	} else if s == "linkedCell" {
		for _, linkedCell := range c.linkedCell {
			toPrint += linkedCell.Print("xy")
		}
	}
	return toPrint
}

func main() {
	input := ReadInput()
	for _, line := range input {
		for _, c := range line {
			fmt.Print(string(c.value) + " ")
		}
		fmt.Println()
	}
	grid := updateIsolation(input)

	for _, line := range grid {
		for _, c := range line {

			if c.isolated {
				fmt.Print("i ")
			} else {
				fmt.Print(string(c.value) + " ")
			}

		}
		fmt.Println()
	}

	isolatedNumbers := FindIsolated(grid)
	fmt.Println("isolatedCells:")
	for i, number := range isolatedNumbers {
		if i%6 == 0 {
			fmt.Println()
		}
		fmt.Print(number.Print("xy"), "\t")
	}
}

func ReadInput() [][]Cell {

	// Read the lines
	s := bufio.NewScanner(os.Stdin)
	input := make([]string, 0)
	for s.Scan() {
		inputLine := s.Text()
		if inputLine == "" {
			break
		}
		input = append(input, inputLine)
	}

	// Create the output slice
	result := make([][]Cell, 0) // Inizializza sl
	for row, inputLine := range input {
		newRow := make([]Cell, 0) // Inizializza ogni sl[i]
		for column, c := range inputLine {
			newNumber := Cell{
				value:      c,
				row:        row,
				column:     column,
				linkedCell: make(map[string]Cell),
			}
			if unicode.IsNumber(newNumber.value) {
				newNumber.isolated = true
			}
			newRow = append(newRow, newNumber)
		}
		result = append(result, newRow)
	}
	return result
}

func updateIsolation(grid [][]Cell) (updatedGrid [][]Cell) {

	// Create a new grid with the same dimensions
	updatedGrid = make([][]Cell, len(grid))
	for i := range grid {
		updatedGrid[i] = make([]Cell, len(grid[i]))
		copy(updatedGrid[i], grid[i])
	}

	// The cells are created with a default isolated=true
	// So we need to find the cases in which it changes
	// Those cases happen when there's a symbol (except a dot) on the surrounding cells

	// Iterate through each cell on the grid to filter
	for lineIndex, line := range updatedGrid {
		for columnIndex, cell := range line {

			fmt.Printf("\nCHECKING THE GRID: cell %s\n", cell.Print("xy"))

			// Check only the numeric cells surroundings that weren't yet checked
			if unicode.IsNumber(cell.value) {

				// The surroundings are delimited by the following indexes
				for i := lineIndex - 1; i <= lineIndex+1; i++ {
					for j := columnIndex - 1; j <= columnIndex+1; j++ {

						fmt.Printf("\tChecking surrounding cell (%d, %d)\n", i, j)

						// Skip if the indexes are out of bounds
						if i < 0 || i >= len(grid) || j < 0 || j >= len(line) {
							fmt.Println("\t\tSkipping (out of bounds)")
							continue
						}

						// Skip the center cell itself
						if i == lineIndex && j == columnIndex {
							fmt.Println("\t\tSkipping (center cell itself)")
							continue
						}

						surroundingCell := grid[i][j]
						// Skip if the surounding cell is a dot
						if surroundingCell.value == '.' {
							fmt.Println("\t\tSkipping (dot)")
							continue
						}

						// If the surrounding cell is a number, link them
						if unicode.IsNumber(surroundingCell.value) && j > columnIndex {
							fmt.Println("\t\tIt's a number, let's link it:")
							LinkCells(&cell, &surroundingCell)
						}

						// If the surrounding cell contains a non-number and non-dot, it's not isolated (it's a symbol)
						if !unicode.IsNumber(surroundingCell.value) && !(surroundingCell.value == '.') {
							fmt.Println("\t\tNot a number, nor a dot")
							FalsifyIsolation(&cell)
							// In that case, all linked cells will be also considered non-isolated
							fmt.Println("\t\t\tApplying isolation criteria to links:")
							FalsifyLinksIsolation(&cell)
						}
					}

				}
			} else {
				if !unicode.IsNumber(cell.value) {
					fmt.Printf("\tSkipping (!number)\n")
				} else if cell.checked {
					fmt.Printf("\tSkipping (already checked)\n")
				}
			}

		}
	}
	return updatedGrid
}

func FindIsolated(grid [][]Cell) []Cell {
	// Now take the isolated cells and return them inside a slice
	isolatedCells := make([]Cell, 0)
	fmt.Printf("Creating the result slice.\n")
	for _, line := range grid {
		for _, cell := range line {
			if cell.isolated {
				fmt.Printf("\tAdding %s to the slice", cell.Print("xy"))
				isolatedCells = append(isolatedCells, cell)
			}
		}
	}
	return isolatedCells
}
