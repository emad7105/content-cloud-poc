package abac_policies.accountstates

default allow_partial = false

allow_partial {
  input.action == "GET"
  data.accountState.broker.id == input.brokerId
}