# Build stage

# Use the specified Node.js version to build the app
FROM node:20.18.0 AS builder

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the code
COPY . .

# Build the app
RUN npm run build

# Production Stage
FROM nginx:alpine

# Copy the build files from the builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Launch nginx server
ENTRYPOINT ["nginx", "-g", "daemon off;"]
