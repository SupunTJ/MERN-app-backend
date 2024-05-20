FROM node:alpine
WORKDIR /server
COPY package*.json ./
RUN npm install
COPY . .
CMD ["npm","run","dev"]

# FROM node:alpine
# WORKDIR /server
# COPY ./package.json ./
# COPY ./package-lock.json ./
# RUN npm install
# COPY ./src ./src
# COPY ./.env ./
# COPY ./config.js ./
# CMD ["npm", "start"]