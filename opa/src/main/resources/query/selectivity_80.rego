package accountstates

default allow = false

allow {
  data.accountState.selectivity80 == token.payload.select_80
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}