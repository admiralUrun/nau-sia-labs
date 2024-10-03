package main

import (
	"crypto/sha256"
	"encoding/hex"
	"strconv"
)

func hashNumber(n int64) string {
	h := sha256.New()
	h.Write([]byte(strconv.FormatInt(n, 10)))
	return hex.EncodeToString(h.Sum(nil))
}
