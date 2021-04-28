package accountstates

default allow = false

allow {
  data.accountState.selectivity0_1 == token.payload.select_0_1
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}