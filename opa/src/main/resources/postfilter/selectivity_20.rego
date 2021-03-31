package accountstates

default allow = false

allow {
  input.accountState.selectivity20 == token.payload.select_20
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}