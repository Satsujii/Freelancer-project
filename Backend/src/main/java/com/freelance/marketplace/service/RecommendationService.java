package com.freelance.marketplace.service;

import com.freelance.marketplace.dto.JobPostResponse;
import com.freelance.marketplace.entity.FreelancerProfile;
import com.freelance.marketplace.entity.JobPost;
import com.freelance.marketplace.entity.JobStatus;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.repository.FreelancerProfileRepository;
import com.freelance.marketplace.repository.JobApplicationRepository;
import com.freelance.marketplace.repository.JobPostRepository;
import com.freelance.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private FreelancerProfileRepository freelancerProfileRepository;
    @Autowired
    private JobPostRepository jobPostRepository;
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobService jobService;

    public List<JobPostResponse> recommendJobsForFreelancer(Long userId) {

        FreelancerProfile profile = freelancerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Freelancer profile not found"));


        List<JobPost> openJobs = jobPostRepository.findByStatus(JobStatus.OPEN);


        Set<Long> appliedJobIds = jobApplicationRepository.findByFreelancerId(userId)
                .stream().map(app -> app.getJob().getId()).collect(Collectors.toSet());
        List<JobPost> availableJobs = openJobs.stream()
                .filter(job -> !appliedJobIds.contains(job.getId()))
                .collect(Collectors.toList());


        String profileText = String.join(" ", profile.getSkills() != null ? profile.getSkills() : Collections.emptyList())
                + " " + (profile.getBio() != null ? profile.getBio() : "");


        Map<String, Double> profileTfidf = computeTfidf(profileText, availableJobs.stream().map(JobPost::getDescription).collect(Collectors.toList()));
        List<JobScore> scoredJobs = new ArrayList<>();
        for (JobPost job : availableJobs) {
            Map<String, Double> jobTfidf = computeTfidf(job.getDescription(), availableJobs.stream().map(JobPost::getDescription).collect(Collectors.toList()));
            double similarity = cosineSimilarity(profileTfidf, jobTfidf);
            scoredJobs.add(new JobScore(job, similarity));
        }


        return scoredJobs.stream()
                .sorted((a, b) -> Double.compare(b.similarity, a.similarity))
                .limit(5)
                .map(js -> jobService.getJob(js.job.getId()))
                .collect(Collectors.toList());
    }


    private Map<String, Double> computeTfidf(String doc, List<String> allDocs) {
        List<String> tokens = tokenize(doc);
        Set<String> vocab = new HashSet<>(tokens);
        Map<String, Double> tfidf = new HashMap<>();
        for (String term : vocab) {
            double tf = Collections.frequency(tokens, term) / (double) tokens.size();
            double idf = Math.log((double) allDocs.size() / (1 + docFreq(term, allDocs)));
            tfidf.put(term, tf * idf);
        }
        return tfidf;
    }


    private int docFreq(String term, List<String> docs) {
        int count = 0;
        for (String doc : docs) {
            if (tokenize(doc).contains(term)) count++;
        }
        return count;
    }


    private List<String> tokenize(String text) {
        if (text == null) return Collections.emptyList();
        return Arrays.stream(text.toLowerCase().split("\\W+")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }


    private double cosineSimilarity(Map<String, Double> v1, Map<String, Double> v2) {
        Set<String> allTerms = new HashSet<>();
        allTerms.addAll(v1.keySet());
        allTerms.addAll(v2.keySet());
        double dot = 0, norm1 = 0, norm2 = 0;
        for (String term : allTerms) {
            double a = v1.getOrDefault(term, 0.0);
            double b = v2.getOrDefault(term, 0.0);
            dot += a * b;
            norm1 += a * a;
            norm2 += b * b;
        }
        return (norm1 == 0 || norm2 == 0) ? 0 : dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private static class JobScore {
        JobPost job;
        double similarity;
        JobScore(JobPost job, double similarity) {
            this.job = job;
            this.similarity = similarity;
        }
    }
} 