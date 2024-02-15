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
	for i := row - 1; i <= row+1; i++ {
		for j := column - 1; j <= column+1; j++ {
			// Skip if out of the grid
			if i < 0 || j < 0 || i > len(grid)-1 || j > len(grid[i])-1 {
				continue
			}

			cellBeingVerified := grid[i][j]
			// Skip the verification on the cell itself, or if it's a number
			if unicode.IsNumber(cellBeingVerified) {
				continue
			}
			// If the surrounding is not ".", the cell is not isolated
			if grid[i][j] != '.' {
				return false
			}
		}
	}
	return true
}

// CheckGrid find the isolated numbers inside a grid and return them in a []string
func CheckGrid(grid [][]rune) []string {
	isolatedNumbers := make([]string, 0)
	for i, line := range grid {
		for j, _ := range line {
			if isIsolated(grid, i, j) {
				isolatedNumbers = append(isolatedNumbers, string(grid[i][j]))
			}
		}
	}
	return isolatedNumbers
}
