#!/usr/bin/env bash

# SNS
echo 'Creating SNS topics'
awslocal sns create-topic --name inventory-events --region eu-west-1 --attributes FifoTopic=false
awslocal sns create-topic --name loan-events --region eu-west-1 --attributes FifoTopic=false
awslocal sns create-topic --name fine-events --region eu-west-1 --attributes FifoTopic=false

echo 'Listing SNS topics'
awslocal sns list-topics

awslocal sqs create-queue --queue-name inventory-queue --region eu-west-1
awslocal sqs create-queue --queue-name loan-queue --region eu-west-1
awslocal sqs create-queue --queue-name fine-queue --region eu-west-1


