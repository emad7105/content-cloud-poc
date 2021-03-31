package accountstates

default allow = false

allow {
  input.accountState.selectivity_40 == token.payload.select_40
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}