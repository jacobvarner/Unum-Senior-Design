import random
def lambda_handler(event, context):
    #TODO: Insert parsing of GET request. Use information from Process assigmnent
    random.seed()
    itemno = random.randint(0, 3)
    if itemno == 0:
        values = "Your claim has been approved."
    elif itemno == 1:
        values = "Unum does not have a claim application on file for you, please contact customer support at 555-555-5555."
    elif itemno == 2:
        values = "Your claim is currently being processed."
    elif itemno == 3:
        values = "Your claim has been processed. Please contact customer service at 555-555-5555 for more information."
    json = {"version": "string", "response": {"outputSpeech": {"type": "PlainText","text": values},"card":{"type": "Simple","title": "Unum","content": values},"shouldEndSession": 'true'}}
    return json
