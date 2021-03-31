package accountstates

default allow = false

allow {
  data.accountState.selectivity10 == token.payload.select_10
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}