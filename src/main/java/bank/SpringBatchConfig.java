package bank;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
    @Autowired
    private JobBuilderFactory jobs;
    @Autowired
    private StepBuilderFactory steps;
    @Autowired
    private ItemReader<BankTransaction> itemReader;
    @Autowired
    private ItemProcessor<BankTransaction,BankTransaction> itemProcessor;
    @Autowired
    private ItemWriter<BankTransaction> itemWriter;

    public SpringBatchConfig(){
    }

    @Bean
    public Job bankJob(){
        Step step = steps.get("bankStep")
                .<BankTransaction,BankTransaction>chunk(10)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
        return  jobs.get("bankJob").start(step).build();
    }

    @Bean
    public FlatFileItemReader<BankTransaction> flatFileItemReader(@Value("${inputFile}") Resource resource){
        FlatFileItemReader<BankTransaction> bankTransactionFlatFileItemReader= new FlatFileItemReader<>();
        bankTransactionFlatFileItemReader.setName("FFIR");
        bankTransactionFlatFileItemReader.setLinesToSkip(1);
        bankTransactionFlatFileItemReader.setResource(resource);
        bankTransactionFlatFileItemReader.setStrict(false);
        bankTransactionFlatFileItemReader.setLineMapper(lineMapper());
        return bankTransactionFlatFileItemReader;

    }
@Bean
    public LineMapper<BankTransaction> lineMapper() {
        DefaultLineMapper<BankTransaction> lineMapper= new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("id","accountId","strTransacionDate","transType","amount");
        lineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper<BankTransaction>fieldSetMapper=new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BankTransaction.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public ItemProcessor<BankTransaction,BankTransaction> process(){
        return bankTransaction -> {
            bankTransaction.setTransDate(new SimpleDateFormat("dd/MM/yyyy-HH:mm").parse(bankTransaction.getStrTransacionDate()));
            return bankTransaction;
        };
    }

    @Bean
    public ItemWriter<BankTransaction> writer(){
        return new ItemWriter<BankTransaction>() {
            @Override
            public void write(List<? extends BankTransaction> list) throws Exception {
                System.out.println("writer.....");
                list.forEach(System.out::println);
            }
        };
    }
}
