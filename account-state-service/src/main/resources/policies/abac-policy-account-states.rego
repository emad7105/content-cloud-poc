package accountstates

default allow_partial = false

allow_partial {
  data.accountState.broker.id == token.payload.brokerId
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}