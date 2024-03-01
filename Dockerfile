FROM pks102/assignment-attendance.cmd:1.0.0

# Set timezone environment variable (replace 'your_timezone' with the desired timezone)
ENV TZ=Asia/Kolkata

# Switch to the root user
USER root

# Install ntpdate
RUN apt-get update && apt-get install -y ntpdate

# Synchronize the system time with an NTP server
RUN ntpdate -u pool.ntp.org

# Update package lists
RUN apt-get update

# Install necessary packages
RUN apt-get install -y tzdata

# Set the timezone
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Switch back to the original user if necessary (replace 'original_user' with the actual username)
USER original_user
