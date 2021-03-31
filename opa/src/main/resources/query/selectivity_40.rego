package accountstates

default allow = false

allow {
  data.accountState.selectivity40 == token.payload.select_40
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}