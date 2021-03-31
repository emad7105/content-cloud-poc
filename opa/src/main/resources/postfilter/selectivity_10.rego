package accountstates

default allow = false

allow {
  input.accountState.selectivity_10 == token.payload.select_10
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}