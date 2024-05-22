package main

import (
	"bufio"
	"fmt"
	"os"
	"unicode"
)

type Number struct {
	value    string
	row      int
	column   int
	isolated bool
}

func main() {

	input := ReadInput()
	for _, line := range input {
		fmt.Println(line)
	}

}

func ReadInput() [][]string {
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
	result := make([][]string, 0)
	for _, readLine := range lines {
		resultLine := make([]string, 0)
		for _, c := range readLine {
			resultLine = append(resultLine, string(c))
		}
		result = append(result, resultLine)
	}
	return result
}

func FilterLine(line []string, target string) []int {
	indexes := make([]int, 0)
	for i, cell := range line {
		switch target {
		case "numbers":
			if unicode.IsNumber(rune(cell[0])) {
				indexes = append(indexes, i)
			}
		case "dots":
			if rune(cell[0]) == '.' {
				indexes = append(indexes, i)
			}
		case "symbols":
			if !(unicode.IsNumber(rune(cell[0]))) && !(rune(cell[0]) == '.') {
				indexes = append(indexes, i)
			}
		}
	}
	return indexes
}

func CheckGrid(input [][]string) []string {

	isolatedNumbers := make([]string, 0)
	for i, line := range input {
		// Find the numbers and the symbols on the line
		lineNumbers, lineSymbols := FilterLine(line, "numbers"), FilterLine(line, "symbols")

		// Look around for the symbols
		aboveSymbols, belowSymbols := make([]int, 0), make([]int, 0)
		if i != 0 {
			aboveSymbols = FilterLine(input[i+1], "symbols")
		}
		if i != len(input)-1 {
			belowSymbols = FilterLine(input[i+1], "symbols")
		}

		// Check the vicinities
		for _, numberIndex := range lineNumbers {

			// Set the checking area
			checkBeginning, checkEnding := numberIndex-1, numberIndex+1

			if unicode.IsNumber(rune(line[numberIndex+1][0])) {
				checkEnding++
			}

			if checkBeginning < 0 {
				checkBeginning = 0
			}
			if checkEnding > len(line)-1 {
				checkEnding = len(line) - 1
			}

			// And check
			for _, symbolIndex := range aboveSymbols {
				if symbolIndex > checkBeginning && symbolIndex < checkEnding {
					continue
				}
			}

			isolatedNumbers = append(isolatedNumbers)
		}

	}
}
