# GAg Predictor

## How to implement a GAg

1) Use a register as a global branch history
2) Use a Cache as a predication history table

### Task 1 : Predict

predict the branch result based on GAg model

HINT : 
1) read from BHR register
2) read the associated block with the BHR value
3) load the read block from the cache into the SC register
4) return the MSB of the read block or SC register

### Task 2 : Update

update the predictor state based on the actual result

HINT : 
1) pass the SC register bits to a saturating counter
2) save the updated value into the cache via BHR
3) update the BHR with the actual branch result

<img src="../../../../../../resources/GAg/GAg.jpg">

