package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
	"unicode"
)

// Define the limit for each cube color
var redLimit int = 12
var greenLimit int = 13
var blueLimit int = 14

type Set struct {
	Red   int
	Green int
	Blue  int
}

func main() {
	lines := ReadLines()
	games := ReadGames(lines)
	for key, values := range games {
		fmt.Println("\nGame", key)
		for _, set := range values {
			fmt.Println(set)
		}
	}
	fmt.Println("\nThe possible games sum is", CountGames(games))

	fmt.Println("The sum of powers is", SumOfPowers(games))
}

// ReadLines returns []string with the read lines, each describing one game
func ReadLines() []string {
	scanner := bufio.NewScanner(os.Stdin)

	// Read the lines containing the games
	lines := make([]string, 0)
	for scanner.Scan() {
		line := scanner.Text()
		if line == "" {
			break
		}
		lines = append(lines, line)
	}
	return lines
}

// ReadGames take the read lines and returns a map organizing the information in a map
// The map's key represents the game number, and the value contains the game's sets
// The []string value separates the sets one by one
func ReadGames(inputLines []string) map[int][]Set {
	games := make(map[int][]Set)
	for _, line := range inputLines {
		var sets []string
		var gameNumber int

		// Find where the game sets description begins and, by doing so, also the game number
		// The flag point will be the ": " after the game number
		s := strings.Split(line, ": ")

		// Find the gameNumber inside s[0]
		for i, c := range s[0] {
			if unicode.IsSpace(c) {
				gameNumber, _ = strconv.Atoi(s[0][i+1:])
			}
		}

		// Organize the sets contained in s[1]
		sets = strings.Split(s[1], "; ")

		// Organize everything inside the map
		games[gameNumber] = FormatSets(sets)
	}
	return games
}

// FormatSets organizes the string sets into Set structures
func FormatSets(s []string) []Set {
	sets := make([]Set, 0)
	for _, setString := range s {
		var red, green, blue int
		setData := strings.Split(setString, ", ")
		for _, el := range setData {

			wheresRed := strings.Index(el, " red")
			if wheresRed != -1 {
				red, _ = strconv.Atoi(el[:wheresRed])
			}

			wheresGreen := strings.Index(el, " green")
			if wheresGreen != -1 {
				green, _ = strconv.Atoi(el[:wheresGreen])
			}

			wheresBlue := strings.Index(el, " blue")
			if wheresBlue != -1 {
				blue, _ = strconv.Atoi(el[:wheresBlue])
			}
		}
		set := Set{red, green, blue}
		sets = append(sets, set)
	}
	return sets
}

// isPossible returns true if the game is possible, false otherwise
func isPossible(game []Set) bool {
	for _, set := range game {
		if set.Red > redLimit || set.Green > greenLimit || set.Blue > blueLimit {
			return false
		}
	}
	return true
}

// CountGames returns the sum of the game number identifiers of all possible game
func CountGames(games map[int][]Set) int {
	var sum int
	for gameNumber, game := range games {
		if isPossible(game) {
			sum += gameNumber
		}
	}
	return sum
}

// GameMinSet returns the minimum number of cubes (of each color) required to make a game possible
func GameMin(game []Set) Set {
	var minRed, minGreen, minBlue int
	for _, set := range game {

		if set.Red > minRed {
			minRed = set.Red
		}

		if set.Green > minGreen {
			minGreen = set.Green
		}

		if set.Blue > minBlue {
			minBlue = set.Blue
		}
	}
	minSet := Set{minRed, minGreen, minBlue}
	return minSet
}

// GameMinPower returns sum of the multiplications between the minimun number of
// cubes (of each color) required to make the games possible
func SumOfPowers(games map[int][]Set) int {
	var sumOfPowers int
	for gameNum, sets := range games {
		fmt.Println("\nFor the game", gameNum)
		gameMinSet := GameMin(sets)
		fmt.Println("The min set is", gameMinSet)
		gamePower := gameMinSet.Red * gameMinSet.Green * gameMinSet.Blue
		fmt.Println("The power is", gamePower)
		sumOfPowers += gamePower
		fmt.Println("The new sum is", sumOfPowers)
	}
	return sumOfPowers
}
