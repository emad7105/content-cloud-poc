package accountstates

default allow = false

allow {
  input.accountState.selectivity_60 == token.payload.select_60
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}