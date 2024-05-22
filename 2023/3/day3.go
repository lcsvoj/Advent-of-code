package main

import (
	"bufio"
	"fmt"
	"os"
	"unicode"
)

func main() {
	input := ReadInput()
	for _, line := range input {
		for _, c := range line {
			fmt.Print(string(c) + " ")
		}
		fmt.Println()
	}
	fmt.Println()
	fmt.Println("IsolatedNumbers =", CheckGrid(input))
}

// ReadInput returns the read data, in the form of a bidimensional slice of strings
// The returned 2D slice will be considered a coordinate grid, with rows and columns
func ReadInput() [][]rune {
	// Read the lines
	s := bufio.NewScanner(os.Stdin)
	lines := make([]string, 0)
	for s.Scan() {
		readLine := s.Text()
		if readLine == "" {
			break
		}
		lines = append(lines, readLine)
	}

	// Separate the columns
	result := make([][]rune, 0)
	for _, readLine := range lines {
		resultLine := make([]rune, 0)
		for _, c := range readLine {
			resultLine = append(resultLine, c)
		}
		result = append(result, resultLine)
	}
	return result
}

// isIsolated checks surroundings of the cell determined by the coordinates row and column on the passed grid
// if it has no symbols around it, it returns false, and true if it doesn't
func isIsolated(grid [][]rune, row, column int) bool {

	startRow, endRow := row-1, row+1
	startColumn, endColumn := column-1, column+1
	fmt.Printf("\n\tisIsolated (%d, %d) ?\n", row, column)
	fmt.Printf("\tstartRow = %d \t endRow = %d \t startColumn = %d \t endColumn = %d\n", startRow, endRow, startColumn, endColumn)

	for i := startRow; i <= endRow; i++ {
		for j := startColumn; j <= endColumn; j++ {

			fmt.Printf("\t\tCurrently checking (%d, %d)\n", i, j)

			// Skip if out of the grid
			if i < 0 || j < 0 || i > len(grid)-1 || j > len(grid[i])-1 {
				fmt.Println("\t\t\tContinued because it's out of bounds")
				continue
			}

			if grid[i][j] == '.' {
				fmt.Println("\t\t\tContinuing because the cell is a dot")
				continue
			}

			cellBeingVerified := grid[i][j]
			// Skip the verification on the cell itself
			if unicode.IsNumber(cellBeingVerified) && (i == row && j == column) {
				fmt.Println("\t\t\tContinued because it's the cell itself")
				continue
			}

			if unicode.IsNumber(cellBeingVerified) {
				// endColumn++
				// fmt.Println("\t\t\tUpdating endColumn to", endColumn)
				continue
			}

			// If the surrounding is not ".", the cell is not isolated
			if grid[i][j] != '.' {
				fmt.Println("\t\t\tReturning FALSE because the cell is not a dot\n")
				return false
			}
		}
	}
	fmt.Println("\tReturning TRUE\n")
	return true
}

// CheckGrid find the isolated numbers inside a grid and return them in a []string
func CheckGrid(grid [][]rune) []string {
	isolatedNumbers := make([]string, 0)
	for i, line := range grid {
		for j, _ := range line {

			fmt.Printf("CHECKING THE GRID: (%d, %d)\n", i, j)
			if !unicode.IsNumber(grid[i][j]) {
				fmt.Println("Continuing because the cell is not numeric")
				continue
			}

			if isIsolated(grid, i, j) {
				fmt.Println("Isolated number FOUND")
				isolatedNumbers = append(isolatedNumbers, string(grid[i][j]))
				fmt.Println(isolatedNumbers)
			}
		}
	}
	return isolatedNumbers
}
