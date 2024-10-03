package main

import (
	"bufio"
	"crypto/rand"
	"fmt"
	"math/big"
	"net"
	"strconv"
	"strings"
)

type User struct {
	ID string
}

var users = map[string]User{
	"user1": {ID: "user1"},
	"user2": {ID: "user2"},
}

func authenticate(userID string, clientHash string, randomNumber int64) bool {
	if _, ok := users[userID]; ok {
		serverHash := hashNumber(randomNumber)
		return serverHash == clientHash
	}
	return false
}

func handleConnection(conn net.Conn) {
	defer conn.Close()

	fmt.Println("Новий клієнт підключився:", conn.RemoteAddr().String())

	reader := bufio.NewReader(conn)

	userID, _ := reader.ReadString('\n')
	userID = strings.TrimSpace(userID)

	randomNumber, _ := rand.Int(rand.Reader, big.NewInt(1000))
	fmt.Println("Випадкове число:", randomNumber.Int64())

	conn.Write([]byte(strconv.FormatInt(randomNumber.Int64(), 10) + "\n"))

	clientHash, _ := reader.ReadString('\n')
	clientHash = strings.TrimSpace(clientHash)

	if authenticate(userID, clientHash, randomNumber.Int64()) {
		conn.Write([]byte("Authentication successful\n"))
	} else {
		conn.Write([]byte("Authentication failed\n"))
	}
}

func runServer() {
	ln, err := net.Listen("tcp", ":8080")
	if err != nil {
		fmt.Println("Помилка запуску сервера:", err)
		return
	}
	defer ln.Close()

	fmt.Println("Сервер очікує з'єднання на порту 8080...")

	for {
		conn, err := ln.Accept()
		if err != nil {
			fmt.Println("Помилка з'єднання:", err)
			continue
		}
		go handleConnection(conn)
	}
}
