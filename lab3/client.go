package main

import (
	"bufio"
	"fmt"
	"net"
	"os"
	"strconv"
	"strings"
)

func runClient() {
	conn, err := net.Dial("tcp", "localhost:8080")
	if err != nil {
		fmt.Println("Помилка підключення до сервера:", err)
		return
	}
	defer conn.Close()

	reader := bufio.NewReader(os.Stdin)

	fmt.Print("Введіть ідентифікатор користувача: ")
	userID, _ := reader.ReadString('\n')
	conn.Write([]byte(strings.TrimSpace(userID) + "\n"))

	randomNumberStr, _ := bufio.NewReader(conn).ReadString('\n')
	randomNumber, _ := strconv.ParseInt(strings.TrimSpace(randomNumberStr), 10, 64)
	fmt.Println("Отримано випадкове число:", randomNumber)

	hashedNumber := hashNumber(randomNumber)

	conn.Write([]byte(hashedNumber + "\n"))

	response, _ := bufio.NewReader(conn).ReadString('\n')
	fmt.Println("Відповідь сервера:", response)
}
