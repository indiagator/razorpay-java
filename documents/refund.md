## Refunds

### Create a normal refund

```java
String paymentId = "pay_FCXKPFtYfPXJPy";

String jsonRequest = "{\n" +
              "\"amount\": \"100\",\n" +
              "\"speed\": \"normal\",\n" +
              "\"notes\": {\n" +
              "\"notes_key_1\": \"Beam me up Scotty.\",\n" +
              "\"notes_key_2\": \"Engage\"\n" +
              "},\n" +
              "\"receipt\": \"Receipt No. 31\"\n" +
              "}";
              
JSONObject requestRequest = new JSONObject(jsonRequest);
              
Payment payment = instance.payments.refund(paymentId,requestRequest);
```

**Parameters:**

| Name       | Type        | Description                                 |
|------------|-------------|---------------------------------------------|
| paymentId* | string      | The id of the payment                       |
| amount     | integer      | The amount to be captured (should be equal to the authorized amount, in paise) |                       |
| speed      | string      | Here, it must be normal                |
| notes      | array       | A key-value pair                |
| receipt    | string      | A unique identifier provided by you for your internal reference. |

**Response:**
```json
{
  "id": "rfnd_FP8QHiV938haTz",
  "entity": "refund",
  "amount": 500100,
  "receipt": "Receipt No. 31",
  "currency": "INR",
  "payment_id": "pay_FCXKPFtYfPXJPy",
  "notes": [],
  "acquirer_data": {
    "arn": null
  },
  "created_at": 1597078866,
  "batch_id": null,
  "status": "processed",
  "speed_processed": "normal"
}
```
-------------------------------------------------------------------------------------------------------

### Create an instant refund

```java

String paymentId = "pay_FCXKPFtYfPXJPy";

String jsonRequest = "{\n" +
              "  \"amount\": \"100\",\n" +
              "  \"speed\": \"optimum\",\n" +
              "  \"receipt\": \"Receipt No. 31\"\n" +
              "}";
              
JSONObject requestJson = new JSONObject(jsonRequest);
              
Payment payment = instance.payments.refund(paymentId,requestJson);
```

**Parameters:**

| Name       | Type        | Description                                 |
|------------|-------------|---------------------------------------------|
| paymentId* | string      | The id of the payment                       |
| amount     | integer      | The amount to be captured (should be equal to the authorized amount, in paise) |
| speed*     | string      | Here, it must be optimum                    |
| receipt    | string      | A unique identifier provided by you for your internal reference. |

**Response:**
```json
{
  "id": "rfnd_FP8R8EGjGbPkVb",
  "entity": "refund",
  "amount": 500100,
  "currency": "INR",
  "payment_id": "pay_FC8MmhMBZPKDHF",
  "notes": {
    "notes_key_1": "Tea, Earl Grey, Hot",
    "notes_key_2": "Tea, Earl Grey… decaf."
  },
  "receipt": "Receipt No. 31",
  "acquirer_data": {
    "arn": null
  },
  "created_at": 1597078914,
  "batch_id": null,
  "status": "processed",
  "speed_requested": "optimum"
}
```
-------------------------------------------------------------------------------------------------------

### Fetch multiple refunds for a payment

```java
String paymentId = "pay_FIKOnlyii5QGNx";

String jsonRequest = "{\n" +
                 "\"count\" : 1\n" +
               "}";
               
JSONObject requestRequest = new JSONObject(jsonRequest);  
 
List<Payment> payment = instance.payments.fetchAllRefunds(paymentId,requestRequest);
```

**Parameters:**

| Name       | Type      | Description                                      |
|------------|-----------|--------------------------------------------------|
| paymentId* | string      | The id of the payment                       |
| from       | timestamp | timestamp after which the payments were created  |
| to         | timestamp | timestamp before which the payments were created |
| count      | integer   | number of payments to fetch (default: 10)        |
| skip       | integer   | number of payments to be skipped (default: 0)    |

**Refund:**
```json
{
  "entity": "collection",
  "count": 1,
  "items": [
    {
      "id": "rfnd_FP8DDKxqJif6ca",
      "entity": "refund",
      "amount": 300100,
      "currency": "INR",
      "payment_id": "pay_FIKOnlyii5QGNx",
      "notes": {
        "comment": "Comment for refund"
      },
      "receipt": null,
      "acquirer_data": {
        "arn": "10000000000000"
      },
      "created_at": 1597078124,
      "batch_id": null,
      "status": "processed",
      "speed_processed": "normal",
      "speed_requested": "optimum"
    }
  ]
}
 ```
-------------------------------------------------------------------------------------------------------

### Fetch a specific refund for a payment
```java
String paymentId = "pay_FIKOnlyii5QGNx";

String refundId = "rfnd_FP8DDKxqJif6ca";

Payment payment = instance.payments.fetchRefund(paymentId,refundId);
```

**Parameters:**

| Name       | Type        | Description                                 |
|------------|-------------|---------------------------------------------|
| paymentId* | string      | The id of the payment to be fetched        |
| refundId*  | string      | The id of the refund to be fetched           |

**Response:**
```json
{
  "id": "rfnd_FP8DDKxqJif6ca",
  "entity": "refund",
  "amount": 300100,
  "currency": "INR",
  "payment_id": "pay_FIKOnlyii5QGNx",
  "notes": {
    "comment": "Comment for refund"
  },
  "receipt": null,
  "acquirer_data": {
    "arn": "10000000000000"
  },
  "created_at": 1597078124,
  "batch_id": null,
  "status": "processed",
  "speed_processed": "normal",
  "speed_requested": "optimum"
}
```
-------------------------------------------------------------------------------------------------------

### Fetch all refunds
```java
String jsonRequest = "{\n" +
        "\"count\" : 1\n" +
        "}";

JSONObject requestJson = new JSONObject(jsonRequest);
        
List<Refund> refund = instance.refunds.fetchAll(requestJson);
```

**Parameters:**

| Name  | Type      | Description                                      |
|-------|-----------|--------------------------------------------------|
| from  | timestamp | timestamp after which the payments were created  |
| to    | timestamp | timestamp before which the payments were created |
| count | integer   | number of payments to fetch (default: 10)        |
| skip  | integer   | number of payments to be skipped (default: 0)    |

**Response:**
```json
{
  "entity": "collection",
  "count": 2,
  "items": [
    {
      "id": "rfnd_FFX6AnnIN3puqW",
      "entity": "refund",
      "amount": 88800,
      "currency": "INR",
      "payment_id": "pay_FFX5FdEYx8jPwA",
      "notes": {
        "comment": "Issuing an instant refund"
      },
      "receipt": null,
      "acquirer_data": {},
      "created_at": 1594982363,
      "batch_id": null,
      "status": "processed",
      "speed_processed": "optimum",
      "speed_requested": "optimum"
    }
  ]
}
```
-------------------------------------------------------------------------------------------------------

### Fetch particular refund
```java
String refundId = "rfnd_EqWThTE7dd7utf";

List<Refund> refund = instance.refunds.fetch(refundId);
```

**Parameters:**

| Name          | Type        | Description                                 |
|---------------|-------------|---------------------------------------------|
|  refundId*   | string      | The id of the refund to be fetched           |

**Response:**
```json
{
  "id": "rfnd_EqWThTE7dd7utf",
  "entity": "refund",
  "amount": 6000,
  "currency": "INR",
  "payment_id": "pay_EpkFDYRirena0f",
  "notes": {
    "comment": "Issuing an instant refund"
  },
  "receipt": null,
  "acquirer_data": {
    "arn": "10000000000000"
  },
  "created_at": 1589521675,
  "batch_id": null,
  "status": "processed",
  "speed_processed": "optimum",
  "speed_requested": "optimum"
}
```
-------------------------------------------------------------------------------------------------------

### Update the refund
```java
String refundId = "rfnd_EqWThTE7dd7utf";

String jsonRequest "{\n" +
              "  \"notes\": {\n" +
              "    \"notes_key_1\": \"Beam me up Scotty.\",\n" +
              "    \"notes_key_2\": \"Engage\"\n" +
              "  }\n" +
             "}";
              
JSONObject requestJson = new JSONObject(jsonRequest);     
         
Refund refund = instance.Refunds.edit(refundId,requestJson);

```

**Parameters:**

| Name  | Type      | Description                                      |
|-------|-----------|--------------------------------------------------|
| refundId*   | string    | The id of the refund to be fetched     |
| notes* | array  | A key-value pair                                 |

**Response:**
```json
{
  "id": "rfnd_FP8DDKxqJif6ca",
  "entity": "refund",
  "amount": 300100,
  "currency": "INR",
  "payment_id": "pay_FIKOnlyii5QGNx",
  "notes": {
    "notes_key_1": "Beam me up Scotty.",
    "notes_key_2": "Engage"
  },
  "receipt": null,
  "acquirer_data": {
    "arn": "10000000000000"
  },
  "created_at": 1597078124,
  "batch_id": null,
  "status": "processed",
  "speed_processed": "normal",
  "speed_requested": "optimum"
}
```
-------------------------------------------------------------------------------------------------------

**PN: * indicates mandatory fields**
<br>
<br>
**For reference click [here](https://razorpay.com/docs/api/refunds/)**