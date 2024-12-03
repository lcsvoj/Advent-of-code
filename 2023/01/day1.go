package main

import (
	"bufio"
	"fmt"
	"os"
	"unicode"
)

func main() {
	// Open the file
	file, err := os.Open("test.txt")
	if err != nil {
		fmt.Println("Error opening the file:", err)
		return
	}
	defer file.Close() // Ensure the file is closed after the function exits

	// Create a Scanner to read the file
	scanner := bufio.NewScanner(file)

	// Iterate over the lines in the file
	// And save the result for each line as an item in a slice
	var lineResults []int
	for scanner.Scan() {
		line := scanner.Text()
		lineResults = append(lineResults, scanLine(line))
	}

	// Check for errors during Scan
	if err := scanner.Err(); err != nil {
		fmt.Println("Error reading from file:", err)
	}

	// Sum the line results
	var sumOfResults int
	for i, lineResult := range lineResults {
		fmt.Println("Line", i, "result is", lineResult)
		sumOfResults += lineResult
	}
	fmt.Println("Final result:", sumOfResults)
}

func scanLine(line string) int {
	var firstNumber, lastNumber int
	var foundFirstNumber, foundSecondNumber bool
	
	for _, d := range line {
		// identify if the character is a digit
		if unicode.IsNumber(d) {
			// save the 1st and the last ones for each line
			if !foundFirstNumber {
				firstNumber = int(d - '0')
				foundFirstNumber = true
				continue
			} else {
				lastNumber = int(d - '0')
				foundSecondNumber = true
				continue
			}

		}

	}

	if !foundSecondNumber {
		lastNumber = firstNumber

	}

	// build a 2-digit number for each line
	return firstNumber*10 + lastNumber
}
