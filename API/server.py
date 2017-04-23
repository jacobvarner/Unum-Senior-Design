import random
import re

def lambda_handler(event, context):
    try:
        if('querystring' in event):
            eventParms = event['querystring'].strip('{}').replace(',',' ')
            eventDict = dict(re.findall(r'(\S+)=(".*?"|\S+)', eventParms))
            returnValue = generateText(eventParms)
            return returnValue
        else:
            return u"querystring not present\n"
    except:
        return u"failed"

def generateText(values):
    #TODO: Insert parsing of GET request. Use information from Process assigmnent
    random.seed()
    itemno = random.randint(0, 3)
    if itemno == 0:
        ret = "Your claim has been approved."
    elif itemno == 1:
        ret = "Unum does not have a claim application on file for you, please contact customer support at 555-555-5555."
    elif itemno == 2:
        ret = "Your claim is currently being processed."
    elif itemno == 3:
        ret = "Your claim has been processed. Please contact customer service at 555-555-5555 for more information."
    json = ""
    if('Alexa' in values):
        json = {"version": "string", "response": {"outputSpeech": {"type": "PlainText","text": ret},"card":{"type": "Simple","title": "Unum","content": ret},"shouldEndSession": 'true'}}
    if('Google' in values):
        json = {"followupEvent":{},"contextOut":[],"data":{},"type":0,"speech":ret,"source":"apiai-weather-webhook-sample","displayText":ret}
    return json
