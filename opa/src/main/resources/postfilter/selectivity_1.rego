package accountstates

default allow = false

allow {
  input.accountState.selectivity1 == token.payload.select_1
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}