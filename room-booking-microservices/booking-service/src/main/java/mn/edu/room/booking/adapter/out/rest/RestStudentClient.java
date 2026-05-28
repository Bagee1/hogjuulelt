package mn.edu.room.booking.adapter.out.rest;

import mn.edu.room.booking.core.dto.StudentClientResponse;
import mn.edu.room.booking.core.port.out.StudentClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class RestStudentClient implements StudentClient {
    private final RestTemplate restTemplate;
    private final String studentServiceUrl;

    public RestStudentClient(RestTemplate restTemplate, @Value("${services.student-url}") String studentServiceUrl) {
        this.restTemplate = restTemplate;
        this.studentServiceUrl = studentServiceUrl;
    }

    @Override
    public Optional<StudentClientResponse> findById(Long id) {
        try {
            return Optional.ofNullable(restTemplate.getForObject(studentServiceUrl + "/students/{id}", StudentClientResponse.class, id));
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw ex;
        }
    }
}
