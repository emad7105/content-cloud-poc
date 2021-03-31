package accountstates

default allow = false

allow {
  input.accountState.selectivity_100 == token.payload.select_100
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}