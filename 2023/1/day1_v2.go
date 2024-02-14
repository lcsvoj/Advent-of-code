package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
	"unicode"
)

var writtenNumbers = map[string]string{
	"one":   "1",
	"two":   "2",
	"three": "3",
	"four":  "4",
	"five":  "5",
	"six":   "6",
	"seven": "7",
	"eight": "8",
	"nine":  "9",
}

func main() {
	inputLines := ReadLines()
	sum := 0
	for i, line := range inputLines {
		fmt.Println("Line", i+1, "=", FirstAndLast(FindNumbers(line)))
		sum += FirstAndLast(FindNumbers(line))
	}
	fmt.Println(sum)
}

func ReadLines() []string {
	lines := make([]string, 0)
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		line := scanner.Text()
		if line == "" {
			break
		}
		lines = append(lines, line)
	}
	return lines
}

// FindNumbers find the hidden numbers inside a single line and return it as a string
func FindNumbers(s string) string {

	revealedNumbers := RevealNumbers(s)

	// Find the hidden numbers
	hiddenNumber := ""
	for _, c := range revealedNumbers {
		if unicode.IsNumber(c) {
			hiddenNumber = hiddenNumber + string(c)
		}
	}

	if len(hiddenNumber) == 1 {
		hiddenNumber = strings.Repeat(hiddenNumber, 2)
	}

	return hiddenNumber
}

// Replace written numbers for their ordinal form
func RevealNumbers(s string) string {

	var substring, revealedNumbers string
	for _, c := range s {
		if unicode.IsNumber(c) {
			revealedNumbers += string(c)
		} else {
			substring += string(c)
			for writtenNumber, ordinalNumber := range writtenNumbers {
				if strings.Contains(substring, writtenNumber) {
					revealedNumbers = revealedNumbers + ordinalNumber
					substring = substring[len(substring)-2:]
				}
			}
		}
	}
	return revealedNumbers
}

func FirstAndLast(hiddenNumber string) (firsAndLast int) {
	var first, last string
	for i, c := range hiddenNumber {
		if i == 0 {
			first = string(c)
		} else if i == len(hiddenNumber)-1 {
			last = string(c)
		}
	}
	n, _ := strconv.Atoi(first + last)
	return n
}
