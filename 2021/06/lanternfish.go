/*
Instructions:
	- each lanternfish creates a new lanternfish once every 7 days;
	- this process isn't necessarily synchronized between every lanternfish - one lanternfish
	might have 2 days left until it creates another lanternfish, while another might have 4;
	- a new lanternfish would surely need slightly longer before it's capable of producing more
	lanternfish: two more days for its first cycle;
	- a lanternfish that creates a new fish resets its timer to 6, not 7 (because 0 is included
	as a valid timer value);
	- the new lanternfish starts with an internal timer of 8 and does not start counting down
	until the next day;

In a few words: a 0 becomes a 6 and adds a new 8 to the end of the list, while each other number
decreases by 1 if it was present at the start of the day.

How many lanternfish would there be after 80 days if the initial lanterfish have the following
internal timers:
	3,3,5,1,1,3,4,2,3,4,3,1,1,3,3,1,5,4,4,1,4,1,1,1,3,3,2,3,3,4,2,5,1,4,1,2,2,4,2,5,1,2,2,1,1,1,1,4,5,4,3,1,4,4,4,5,1,1,4,3,4,2,1,1,1,1,5,2,1,4,2,4,2,5,5,5,3,3,5,4,5,1,1,5,5,5,2,1,3,1,1,2,2,2,2,1,1,2,1,5,1,2,1,2,5,5,2,1,1,4,2,1,4,2,1,1,1,4,2,5,1,5,1,1,3,1,4,3,1,3,2,1,3,1,4,1,2,1,5,1,2,1,4,4,1,3,1,1,1,1,1,5,2,1,5,5,5,3,3,1,2,4,3,2,2,2,2,2,4,3,4,4,4,1,2,2,3,1,1,4,1,1,1,2,1,4,2,1,2,1,1,2,1,5,1,1,3,1,4,3,2,1,1,1,5,4,1,2,5,2,2,1,1,1,1,2,3,3,2,5,1,2,1,2,3,4,3,2,1,1,2,4,3,3,1,1,2,5,1,3,3,4,2,3,1,2,1,4,3,2,2,1,1,2,1,4,2,4,1,4,1,4,4,1,4,4,5,4,1,1,1,3,1,1,1,4,3,5,1,1,1,3,4,1,1,4,3,1,4,1,1,5,1,2,2,5,5,2,1,5
*/

package main

import (
	"fmt"
	"strconv"
	"strings"
)

// var input string = "3,4,3,1,2"
// var input string = "3,3,5,1,1,3,4,2,3,4,3,1,1,3,3,1,5,4,4,1,4,1,1,1,3,3,2,3,3,4,2,5,1,4,1,2,2,4,2,5,1,2,2,1,1,1,1,4,5,4,3,1,4,4,4,5,1,1,4,3,4,2,1,1,1,1,5,2,1,4,2,4,2,5,5,5,3,3,5,4,5,1,1,5,5,5,2,1,3,1,1,2,2,2,2,1,1,2,1,5,1,2,1,2,5,5,2,1,1,4,2,1,4,2,1,1,1,4,2,5,1,5,1,1,3,1,4,3,1,3,2,1,3,1,4,1,2,1,5,1,2,1,4,4,1,3,1,1,1,1,1,5,2,1,5,5,5,3,3,1,2,4,3,2,2,2,2,2,4,3,4,4,4,1,2,2,3,1,1,4,1,1,1,2,1,4,2,1,2,1,1,2,1,5,1,1,3,1,4,3,2,1,1,1,5,4,1,2,5,2,2,1,1,1,1,2,3,3,2,5,1,2,1,2,3,4,3,2,1,1,2,4,3,3,1,1,2,5,1,3,3,4,2,3,1,2,1,4,3,2,2,1,1,2,1,4,2,4,1,4,1,4,4,1,4,4,5,4,1,1,1,3,1,1,1,4,3,5,1,1,1,3,4,1,1,4,3,1,4,1,1,5,1,2,2,5,5,2,1,5"
var input string = "3,3,2,1,4,1,1,2,3,1,1,2,1,2,1,1,1,1,1,1,4,1,1,5,2,1,1,2,1,1,1,3,5,1,5,5,1,1,1,1,3,1,1,3,2,1,1,1,1,1,1,4,1,1,1,1,1,1,1,4,1,3,3,1,1,3,1,3,1,2,1,3,1,1,4,1,2,4,4,5,1,1,1,1,1,1,4,1,5,1,1,5,1,1,3,3,1,3,2,5,2,4,1,4,1,2,4,5,1,1,5,1,1,1,4,1,1,5,2,1,1,5,1,1,1,5,1,1,1,1,1,3,1,5,3,2,1,1,2,2,1,2,1,1,5,1,1,4,5,1,4,3,1,1,1,1,1,1,5,1,1,1,5,2,1,1,1,5,1,1,1,4,4,2,1,1,1,1,1,1,1,3,1,1,4,4,1,4,1,1,5,3,1,1,1,5,2,2,4,2,1,1,3,1,5,5,1,1,1,4,1,5,1,1,1,4,3,3,3,1,3,1,5,1,4,2,1,1,5,1,1,1,5,5,1,1,2,1,1,1,3,1,1,1,2,3,1,2,2,3,1,3,1,1,4,1,1,2,1,1,1,1,3,5,1,1,2,1,1,1,4,1,1,1,1,1,2,4,1,1,5,3,1,1,1,2,2,2,1,5,1,3,5,3,1,1,4,1,1,4"
var fishInitialValue int = 8
var fishRestartValue int = 6
var daysToGo int = 80

func main() {

	// convert the population data string into []int
	population := makeSlice(input)

	for i := 0; i < daysToGo; i++ {

		// compute how many new fish will be born at the end of this day
		newFish := findNumber(population, 0)

		// update fish data for the next day
		population = minusOne(population)

		// add newborn fish to population and increase day count
		population = addNewFish(population, newFish, fishInitialValue)
	}

	fmt.Printf("After %d days we'll have %d fish.", daysToGo, len(population))
}

// makeSlice converts a given string containing integers separated by commas into a []int
// with the respective integers
func makeSlice(input string) []int {
	var population []int
	inputSl := strings.Split(input, ",")
	for _, c := range inputSl {
		n, _ := strconv.Atoi(c)
		population = append(population, n)
	}
	// fmt.Println("Created []int:", population)
	return population
}

// findNumber receives a []int and a number and returns the number of times the given number
// appears in the []int
func findNumber(sl []int, n int) int {
	var count int
	// fmt.Printf("Searching for occurrences of %v in %v\n", n, sl)
	for _, n := range sl {
		if n == 0 {
			count++
		}
	}
	// fmt.Printf("Found %d occurrences of %v in %v\n", count, n, sl)
	return count
}

// minusOne takes a []int and returns it with all its elements reduced by 1 unit
func minusOne(sl []int) (newSl []int) {
	// fmt.Printf("Reducing in one every value in %v\n", sl)
	for _, n := range sl {
		if n == 0 {
			newSl = append(newSl, fishRestartValue)
		} else {
			newSl = append(newSl, n-1)
		}
	}
	// fmt.Println("New population data:", newSl)
	return newSl
}

// addNewFish receives a []int and two integers: N and T. It then appends N fishes with the
// internal time value of T to the given slice
func addNewFish(sl []int, N int, T int) (newSl []int) {
	newSl = sl
	// fmt.Printf("Adding %d values %d to the slice %v.\n", N, T, sl)
	for i := 0; i < N; i++ {
		newSl = append(newSl, T)
	}
	// fmt.Printf("Added %d values %d to the slice %v.\n", N, T, newSl)
	return newSl
}
